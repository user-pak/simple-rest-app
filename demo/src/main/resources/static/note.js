$(document).ready(function() {
    $.ajax({
        url: "/apiNotes"
    }).then(function(notes) {    
    	console.log(notes);
    	$.each(notes._embedded.apiNotes, function(index, value) {
    		$("#note_body").append("<tr><td><a href='" + value._links.note.href.replace("apiNotes","notes") + "'>" + value.title + "</td></tr>");
    		
    	})
    });
    
    $("#post_note_form").on("submit", function(e) {
       	e.preventDefault();
    	postNote();
    });
});

function postNote() {
	$.ajax({
		url:"/apiNotes",
		method:"post",
		contentType:"application/json",
		data: JSON.stringify({title:$("#title").val(),content:$("#content").val(),writer:$("#nickname").text()}),
		success:function(newNote) {
			console.log(newNote);
			$("#note_body").append("<tr><td><a href='" + newNote._links.note.href.replace("apiNotes","notes") + "'>" + newNote.title + "<small>작성자 " + newNote.writer + "</small></td></tr>");
		}
	})
}	

