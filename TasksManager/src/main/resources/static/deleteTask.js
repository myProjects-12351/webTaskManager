function deleteTask(taskId) {
    if (confirm("Are you sure you want to delete this task?")) {
        $.ajax({
            type: "POST",
            url: "/deleteTask",
            data: { taskId: taskId },
            error: function() {
                alert("An error occurred while deleting the task.");
            }
        });
    }
}

function informAboutTaskID(taskId) {
    alert(taskId)
}

