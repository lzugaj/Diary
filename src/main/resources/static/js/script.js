function deleteNoteConfirmation(noteId) {
    console.log(noteId);
    let confirmation = confirm("Are you sure you want to delete this Note?");
    if (confirmation === true) {
        $.ajax({
            type : "DELETE",
            url : "http://localhost:8080/note/" + noteId,
            contentType: "application/json",
            dataType: "json"
        });
    }
}