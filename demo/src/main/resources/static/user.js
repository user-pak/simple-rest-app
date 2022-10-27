function replaceUserDescription() {
	$.ajax({
		url:"/apiUsers/" + $("#profile_id").val();
		method:"patch",
		contentType:"application/json",
		data:JSON.stringify({description:$("#profile_description").val()}),
		success:function(replacedDescription) {
			
		}
	})
}