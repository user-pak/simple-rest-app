$(document).ready(function() {
	
	const btn = document.createElement("button");
	btn.innerHTML = "게시글 보내기";
	btn.id="post_btn"
	document.body.appendChild(btn);
	
	$.ajax({
		url: "/apiPosts"
	}).then(function(posts) {
		const $ul = document.createElement("ul");
		$.each(posts._embedded.apiPosts, function(index, value) {
			const $li = document.createElement("li");
			const para = document.createElement("a");
			para.href = value._links.post.href.replace("apiPosts","posts");
			para.innerText = value.title;
			document.body.appendChild($li).appendChild(para);
		})
	})
		
	$("#post_btn").on("click", function() {
		$.ajax({
			url:"/apiPosts",
			method:"post",
			contentType:"application/json",
			data:JSON.stringify({title:$("#title").val(),content:$("#content").val()}),
			success:function(post) {
				const $li = document.createElement("li");
				const para = document.createElement("a");
				para.href = post._links.post.href.replace("apiPosts","posts");
				para.innerText = post.title;
				document.body.appendChild($li).appendChild(para);
			}
		})
	})
	
	
})