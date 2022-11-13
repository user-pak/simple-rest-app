$(document).ready(function() {
	
	var name_value = $("#nickname").text();
	var post_id = $("#post_id").val();
	
	$("#post_update").on("click",function() {
		$.ajax({
			url: "/apiPosts/" + post_id,
			method: "patch",
			contentType: "application/json",
			data: JSON.stringify({title:$("#post_title").val(), content:$("#post_content").val()}),
			success: function(replacedPost) {
				alert("게시글이 변경되었습니다");
				location.href="/posts/" + post_id;
			}
		})
	})
	
	$("#post_delete").on("click", function() {
		$.ajax({
			url: "/apiPosts/" + post_id,
			method: "delete",
			success: function() {
				alert("게시글이 삭제되었습니다");
				location.href="/postsAndComments";
			}
		})
	})

	$("#comment_send").on("click", function() {
		$.ajax({
			url: "/apiPosts/" + post_id + "/postComments",
			method: "post",
			contentType: "application/json",
			data:JSON.stringify({name:name_value, review:$("#comment_review").val()}),
			success:function(postWithComment) {
				location.href="/posts/" + post_id;
			}
		})
	})
	
})

function updateReview(element) {
	var comment_id = element.previousElementSibling.value;
	var post_id = $("#post_id").val();
	var comment_replaced_review = element.parentElement.parentElement.firstElementChild.nextElementSibling.value;
	$.ajax({
		url: "/apiPosts/" + post_id + "/postComments/" + comment_id,
		method: "put",
		contentType: "application/json",
		data:JSON.stringify({review:comment_replaced_review}),
		success: function() {
			alert("댓글이 변경되었습니다");
			location.href="/posts/" + post_id;
		}	
	})
}

function deleteComment(element) {
	var comment_id = element.nextElementSibling.value;
	var post_id = $("#post_id").val();
	$.ajax({
		url: "/apiPosts/" + post_id + "/postComments/" + comment_id,
		method: "delete",
		success: function() {
			alert("댓글이 삭제되었습니다");
			location.href="/posts/" + post_id;
		}
	})
}

