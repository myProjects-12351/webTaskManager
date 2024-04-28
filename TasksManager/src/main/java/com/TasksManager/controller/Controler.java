package com.TasksManager.controller;

import com.TasksManager.DatabaseOperations;
import com.TasksManager.entity.Task;
import com.TasksManager.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class Controler {
    @Autowired
    DatabaseOperations databaseOperations;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String login1(){
        return "login";
    }

    @PostMapping("/welcome_logined")
    public String welcome_loginned(@RequestParam("login") String login, @RequestParam("password") String password, HttpSession session) {
        if (databaseOperations.canUserLogin(login, password)) {
            session.setAttribute("userID", databaseOperations.getUserIdByLogin(login));
            session.setAttribute("userLogin", login);
            return "redirect:/mainPanel";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String register(){
        return "register";
    }

    @PostMapping("/register")
    public String register1(){
        return "register";
    }

    @PostMapping("/welcome_registered")
    public String welcome_registered(@ModelAttribute User user, Model model){
        System.out.println(user.toString());
        model.addAttribute("login", user.getLogin());
        model.addAttribute("fname", user.getFname());
        model.addAttribute("lname", user.getLname());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("password", user.getPassword());

        if (!databaseOperations.isUserExist(user))
            databaseOperations.registerToDatabase(user);
        else{
            return "register";
        }

        return "welcomeREG";
    }

    @GetMapping("/mainPanel")
    public String mainPanel(Model model, HttpSession session) {
        Long userID = (Long) session.getAttribute("userID");
        String userLogin = (String) session.getAttribute("userLogin");
        if (userID != null) {
            List<Task> tasks = databaseOperations.getTasksForUserByID(userLogin);
            model.addAttribute("tasks", tasks);
            return "mainPanel";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/addTaskPanel")
    public String addTaskPanel(){
        return "addTask";
    }

    @PostMapping("/addTask")
    public String addTask(HttpSession session, @ModelAttribute Task task, @RequestParam("end_date") String endDateString){
        Long userID = (Long) session.getAttribute("userID");
        String userLogin = (String) session.getAttribute("userLogin");
        if (userID != null) {
            java.sql.Date endDateSql = java.sql.Date.valueOf(endDateString);

            java.util.Date utilDate = new java.util.Date();
            java.sql.Date sqlCurrentDate = new java.sql.Date(utilDate.getTime());

            task.setEndDate(endDateSql);
            task.setCompleted(0);
            task.setStartDate(sqlCurrentDate);
            databaseOperations.addTask(task, userLogin);
        }
        return "redirect:/mainPanel";
    }

    @GetMapping("/deleteTask/{taskId}")
    public String deleteTask(@PathVariable("taskId") Long taskId, HttpSession session){
        String userLogin = (String) session.getAttribute("userLogin");
        System.out.println("Task ID to delete: "+taskId);
        if(userLogin != null){
            databaseOperations.deleteTask(taskId, userLogin);
        }
        return "redirect:/mainPanel";
    }

    @GetMapping("activateTask/{taskId}")
    public String activateTask(@PathVariable("taskId") Long taskId, HttpSession session){
        String username = (String) session.getAttribute("userLogin");
        if(username != null){
            databaseOperations.changeStatus(taskId, username, 0);
        }
        return "redirect:/mainPanel";
    }

    @GetMapping("finishTask/{taskId}")
    public String finishTask(@PathVariable("taskId") Long taskId, HttpSession session){
        String username = (String) session.getAttribute("userLogin");
        if(username != null){
            databaseOperations.changeStatus(taskId, username, 1);
        }
        return "redirect:/mainPanel";
    }
}
