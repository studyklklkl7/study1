/*let selVal = "";

if(page="u"){
	selVal = '{{#authDto.stateCd}}{{authDto.stateCd}}{{/authDto.stateCd}}';
}else{
	selVal ='{{#authCnd.srchStateCd}}{{authCnd.srchStateCd}}{{/authCnd.srchStateCd}}';
}*/

//console.log("selVal == " + selVal);
//console.log("pageNm == " + typeof(pageNm));
const PAGE = {
	title : "권한"
}

$(function(){
	let auth = {
		init: function () {
	        let _this = this;
	        let f = $('form')[0];
	        
	        //리스트 페이지
	        if(typeof(pageNm) != 'undefined' && pageNm == "list"){
				//조회버튼 클릭시
				$(".srch_go").off("click.srch_go").on("click.srch_go",function(e){
					let page = "1";
					_this._go_search(f, page);
				});
				
				//등록버튼 클릭시 페이지 이동
				$(".create_go").off("click.create_go").on("click.create_go",function(e){
					$(f).attr("action","/view/cmn/auth/create");
					$(f).submit();
				});
				
				//상세페이지 이동
				$(".view").off("click.view").on("click.view",function(){
					let authCd = $(this).attr("data-no");
					$("input[name='authCd']").val(authCd);
					
					$(f).attr("action","/view/cmn/auth/update");
					//$(f).attr("action","/view/cmn/auth/update/"+authCd);
					$(f).submit();
				});
			}
			
			//리스트, 수정 페이지
	        if(typeof(pageNm) != 'undefined' && (pageNm == "list" || pageNm == "update")){
				//검색 및 상세페이지 상태값 selected 처리
				$("select[name='srchStateCd'], select[name='stateCd']").find("option").each (function (index, item) {
					let val = $(item).val();
					if(selVal == val){
						$(item).attr("selected", "selected");
					}
				});
			}
	       	
	       	//등록, 수정 페이지
			if(typeof(pageNm) != 'undefined' && (pageNm == "create" || pageNm == "update")){
				//리스트페이지 이동
				$(".list").off("click.list").on("click.list",function(){
					_this._goList(f);
				});
			}
			
			//등록페이지
			if(typeof(pageNm) != 'undefined' && (pageNm == "create" || pageNm == "update")){

				//등록버튼 클릭시
				$(".save").off("click.save").on("click.save",function(e){
					//var f = $("form[name='frm']");
			
					if(!inputNameCheck("authName", "권한명을 입력해주세요.")) return;
					if(!selectNameCheck("stateCd", "사용여부를 선택해주세요.")) return;

					c_confirm("권한", function save_go(){ _this._save(f, pageNm, procUrl, type, txt)}, txt+" 하시겠습니까?");
					
				});//click.save end
			}
		},
		_goList: function(f){		
			//let f = $('form')[0];
			$(f).attr("action","/view/cmn/auth/list");
			$(f).submit();			
		},
		_go_search: function(f, page){
			$("input[name='page']").val(page);
			$(f).submit();			
		},
		_save: function(f, pageNm, url, type, txt){
			let _this = this;
			//return;		
			//var url ="/view/cmn/auth/create-ajax";
			let data = $(f).serialize();
			$.ajax({
				type : type,
				url : url,
				data : data,
				dataType: "json",
				success : function(result){
                	if(result > 0){
                		//c_alert("권한이 등록 되었습니다.", C_OK, undefined, "");
                		c_alert(PAGE.title, C_OK, function list_go(){ _this._goList(f)}, txt+" 되었습니다.");
                	}else{
                		c_alert(PAGE.title, C_FAIL, function(){return false;}, txt+" 실패했습니다.");
                	}
				},beforeSend:function(){
	            	//hideLoading();
	            	showLoading();
	            },complete:function(){
	            	hideLoading();
	            },error: function(xhr, status, err) {
	            	if (xhr.status == 401) {
	    				c_alert(PAGE.title, C_FAIL, function(){return false;}, "접근 권한이 없습니다. 로그인후 다시 시도해 주세요.");
	    				//location.href = "/";
	    			} else if (xhr.status == 403) {
	    				c_alert(PAGE.title, C_FAIL, function(){return false;}, "해당 페이지에 대한 권한이 없습니다.");
	    			} else if (xhr.status == 301) {
	    				c_alert(PAGE.title, C_FAIL, function(){return false;}, "세션이 끊겼습니다.다시 로그인 해주세요.");
	    				//alert("접근 오류가 발생했습니다. 로그인후 다시 시도해 주세요.");
	    				//location.href = "/";
	    			}else {
	    				c_alert(PAGE.title, C_FAIL, function(){return false;}, "처리중 오류가 발생하였습니다. 잠시후 다시 시도해 주세요.");
	    			}
	            }
			});//ajax end		
		}
	};

	auth.init();
});







