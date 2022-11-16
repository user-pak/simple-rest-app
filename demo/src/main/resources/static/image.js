$(document).ready(function() {
	$.ajax({
		url:"/apiImages",
		method:"get",
		contentType:"application/json",
		success:function(images) {

		}
	})
})
function deleteSend(element) {
	if(confirm("삭제하시겠습니까")) {
		var image_id = element.nextElementSibling.value;
		$.ajax({
			url:"/apiImages/" + image_id,
			method:"delete",
			success:function() {
				location.href="/images";
			}
		})
	}
}
function updateTitle(element) {
	var image_id = element.previousElementSibling.value;
	var image_replace_title = element.parentElement.parentElement.firstElementChild.value;
	$.ajax({
		url:"/apiImages/" + image_id,
		method:"patch",
		contentType:"application/json",
		data:JSON.stringify({imageTitle:image_replace_title}),
		success:function() {
			alert("변경되었습니다");
			location.href="/images";
		}
	})
}