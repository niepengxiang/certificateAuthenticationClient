var $modal = $('#myModal');

//发送机构证书同步请求
function submitOrgCredential(){
	$.ajax({
			url:"/data/orgCredential",
			type:"post",
			data:JSON.stringify($("#orgCredentialFrom").serializeObject()),
			dataType:"json",
			contentType:"application/json;charse=UTF-8",
			success: function(msg){
				var htmlStr = returnStr(msg,"org");	
				$modal.find('.modal-body p').html(htmlStr);
				$modal.modal({backdrop: 'static'});	
			}
		});
	return false;
}

//发送个人非机构证书同步请求
function submitOrgPersonCredential(){
	$.ajax({
		url:"/data/orgPersonCredential",
		type:"post",
		data:JSON.stringify($("#orgPersonCredentialFrom").serializeObject()),
		dataType:"json",
		contentType:"application/json;charse=UTF-8",
		success: function(msg){
		
		  var htmlStr = returnStr(msg,"org");	
		  $modal.find('.modal-body p').html(htmlStr);
		  $modal.modal({backdrop: 'static'});
		}
	});
	return false;
}

//发送CA用户同步请求
function submitCaPerson(){
	$.ajax({
		url:"/data/caPerson",
		type:"post",
		data:JSON.stringify($("#caPersonFrom").serializeObject()),
		dataType:"json",
		contentType:"application/json;charse=UTF-8",
		success: function(msg){
		  var htmlStr = returnStr(msg,"caPerson");	
		  $modal.find('.modal-body p').html(htmlStr);
		  $modal.modal({backdrop: 'static'});
		}
	});
	return false;
}


//发送CA用户证书链同步请求
function submitCaCredential(){
	$.ajax({
		url:"/data/caCredential",
		type:"post",
		data:JSON.stringify($("#caCredentialFrom").serializeObject()),
		dataType:"json",
		contentType:"application/json;charse=UTF-8",
		success: function(msg){
		  var htmlStr = returnStr(msg,"caCredential");	
		  $modal.find('.modal-body p').html(htmlStr);
		  $modal.modal({backdrop: 'static'});
		 
		}
	});
	return false;
}