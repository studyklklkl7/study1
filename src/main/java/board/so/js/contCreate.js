
$(function(){
	var auth = {
		init: function () {
	        var _this = this;
	        //var f = $('form')[0];
	        var f = $("form[name='frm']");
	        
	      	$("[name='recruitMgrCd']").select2();
	        
	        //우편번호 찾기
	        $("#zipCode, #addr, #zipCodeBtn").off("click.zipcode").on("click.zipcode",function(e){
				openDaumPostcode();
			});
			
			//채널경로, 모집자 selected
			$("select[name='channelCd'], select[name='recruitMgrCd']").find("option").each (function (index, item) {
				var val = $(item).val();
				var selName = $(this).parent().attr("name");
				var selVal;
							
				if(selName == "channelCd"){
					selVal = chVal;
				}else{
					selVal = reMrgCdVal;
				}	
				
				if(selVal == val){
					$(item).attr("selected", "selected");
				}
			});
			
			//브랜드 선택시 상품 동적 구성
	        $(".srchBrandCd").off("change.srchBrandCd").on("change.srchBrandCd",function(e){
				var val = $(this).val();
				_this._srchPdt(f, val);
			});
			
			//상품 선택시 상품정보 가져오기
	        $(".srchPdtCd").off("change.srchPdtCd").on("change.srchPdtCd",function(e){
				var val = $(this).val();
				_this._srchPdtInfo(f, val);
			});
			
			//상품납입기간 선택시
			$("input[name='pay_type_cd']:radio").off("change.pay_type_cd").on("change.pay_type_cd",function(e){
				var val = $(this).val();
				_this._payTypeCdChange(f, val);
			});
			
			//할인횟수 선택시
	        $("#discountTimes").off("change.discountTimes").on("change.discountTimes",function(e){
				var val = $(this).val();
				_this._discountTimesChange(f, val);
			});
			
			//계좌인증 클릭시
			$(".accountCertify").off("click.accountCertify").on("click.accountCertify",function(e){
				var custNm = $("input[name='custNm']").val(); //계약자명
				if(custNm != ""){
					$("input[name='popDepositor']").val(custNm);
				}
				$("select[name='popBankCd']").val(''); //이체 은행명 리셋
				$("input[name='popAccountNo']").val(''); //계좌번호 리셋
			});
			
			/*계좌인증 팝업*/
			//본인 check, uncheck
			$("input[name='popDepRelTypeCd']").off("change.popDepRelTypeCd").on("change.popDepRelTypeCd",function(e){
				var custNm = $("input[name='custNm']").val(); //계약자명
				var popDepositorAttr = $("input[name='popDepositor']"); //팝업 계약자명 attr
		        if($(this).is(":checked")){
		            $(popDepositorAttr).attr("readonly", true);
		            $(popDepositorAttr).val(custNm);
		            
		        }else{
		            $(popDepositorAttr).attr("readonly", false);
		            $(popDepositorAttr).val('');
		        }
		    });
		    
		    //계좌인증 버튼 클릭 시
	        $(".popAccCert").off("click.popAccCert").on("click.popAccCert",function(e){
				var certf = $("form[name='accountFrm']");
				
				//여기서 부터
				//_this._accountNoCert(certf);
				
			});
			/*계좌인증 팝업*/
			

		},
		_srchPdt: function(f, val){
			var url ="/ext/contract/productList-ajax";
			var data =  {brandCd : val};
			$.ajax({
				type : "POST",
				url : url,
				data : data,
				dataType: "json",
				success : function(array){
					if (array.length > 0) {
						$.each(array, function(i, item){
							var optionStr = "<option value=" + item.pdtCd + ">" + 
												 item.pdtName + 
											"</option>";
							
							$("#pdtCd").append(optionStr);
						})
					} else {
						$("#pdtCd").html('').append("<option value=''>선택</option>");
					}
				},beforeSend:function(){
	            	showLoading();
	            },complete:function(){
	            	hideLoading();
	            },error: function(xhr, status, err) {
	            	if (xhr.status == 401) {
	    				alert("접근 권한이 없습니다. 로그인후 다시 시도해 주세요.");
	    			} else if (xhr.status == 403) {
	    				alert("해당 페이지에 대한 권한이 없습니다.");
	    			} else if (xhr.status == 301) {
	    				alert("세션이 끊겼습니다.다시 로그인 해주세요.");
	    				//alert("접근 오류가 발생했습니다. 로그인후 다시 시도해 주세요.");
	    				//location.href = "/";
	    			}else {
	    				alert("처리중 오류가 발생하였습니다. 잠시후 다시 시도해 주세요.");
	    			}
	            }
			});//ajax end		
		},
		_srchPdtInfo: function(f, val){
			var url ="/ext/contract/productInfo-ajax";
			var data =  {pdtCd : val};
			$.ajax({
				type : "POST",
				url : url,
				data : data,
				dataType: "json",
				success : function(result){				
					if(result){
/*console.log("result.pdtName ==" + result.pdtName);
console.log("result.pdtDescript ==" + result.pdtDescript);
console.log("result.pdtUnitPrice ==" + result.pdtUnitPrice);
console.log("result.strPdtUnitPrice ==" + result.strPdtUnitPrice);
console.log("result.payTimes ==" + result.payTimes);
console.log("result.strMonthPayAmount ==" + result.strMonthPayAmount);
console.log("result.discountNumMin ==" + result.discountNumMin);
console.log("result.discountNumMax ==" + result.discountNumMax);*/
						if(typeof result.pdtName != "undefined"){ //상품 이름 없다면, 모든 정보도 없다
							$(".pdtInfo").find(".pdtName").html('').append(result.pdtName);
							$(".pdtInfo").find(".pdtDescript").html('').append(result.pdtDescript);
							$("input[name='pdtUnitPrice']").val(result.pdtUnitPrice);
							$("input[name='strPdtUnitPrice']").val(result.strPdtUnitPrice);
							
							$(".payTypeCdMonth").html('').append('월납('+result.payTimes+'회)');
							$("input[name='orginPayTimes']").val(result.payTimes);
							
							$("input[name='strMonthPayAmount']").val(result.strMonthPayAmount);
							$("input[name='monthPayAmount']").val(result.monthPayAmount);
							
							var discountNumMin = result.discountNumMin;
							var discountNumMax = result.discountNumMax;
									
							if(typeof result.discountNumMax == "undefined" || discountNumMax < 1){
								$("#discountTimes").html('').append("<option value=''>선택</option>");
								$("input[name='payTimes']").val('');
							}else{
								for(var i= discountNumMin; i<=discountNumMax; i++){
									var optionStr = "<option value=" + i + ">" + 
													 i + 
												"</option>";
								
									$("#discountTimes").append(optionStr);
								}
							}
						}else{
							_pdtInfoReset(); //상품정보 리셋
						}
							
					}
				},beforeSend:function(){
	            	showLoading();
	            },complete:function(){
	            	hideLoading();
	            },error: function(xhr, status, err) {
	            	if (xhr.status == 401) {
	    				alert("접근 권한이 없습니다. 로그인후 다시 시도해 주세요.");
	    			} else if (xhr.status == 403) {
	    				alert("해당 페이지에 대한 권한이 없습니다.");
	    			} else if (xhr.status == 301) {
	    				alert("세션이 끊겼습니다.다시 로그인 해주세요.");
	    				//alert("접근 오류가 발생했습니다. 로그인후 다시 시도해 주세요.");
	    				//location.href = "/";
	    			}else {
	    				alert("처리중 오류가 발생하였습니다. 잠시후 다시 시도해 주세요.");
	    			}
	            }
			});//ajax end		
		},
		_payTypeCdChange: function(f, val){
			var orginPayTimesVal = $("input[name='orginPayTimes']").val(); //납입횟수
			var discountTimesVal = $("select[name='discountTimes']").val(); //할인횟수
			var pdtUnitPriceVal = $("input[name='pdtUnitPrice']").val();	//상품금액
			var monthPayAmountVal = $("input[name='monthPayAmount']").val();	//월납입액
			var payTimes, totalPayAmountVal;
			var times = 150;
			if(val == "2"){ //월납일 경우
				$(".mpayamountYN").show();	//월납입액 입력필드 노출
				$(".payTimesYN").show();	//가입납입기간 입력필드 노출
				if(discountTimesVal != ""){
					payTimes = orginPayTimesVal - discountTimesVal; //가입납입기간
					$("input[name='payTimes']").val(payTimes);
					totalPayAmountVal = payTimes * monthPayAmountVal; //가입금액 = 가입납입기간 * 월납입액
					$("input[name='totalPayAmount']").val(totalPayAmountVal);
				}
			}else{ //일시납일 경우
				$(".mpayamountYN").hide();	//월납입액 입력필드 비노출
				$(".payTimesYN").hide(); //가입납입기간 입력필드 비노출
				
				if(discountTimesVal != ""){	 //선택된 할인 횟수 값이 있을 경우		
					totalPayAmountVal = pdtUnitPriceVal - (discountTimesVal * (pdtUnitPriceVal/times));	//가입금액 =  상품금액 - {할인횟수 × (상품금액 ÷ 150)}
					$("input[name='totalPayAmount']").val(totalPayAmountVal);
				}
				
			}
		},
		_discountTimesChange: function(f, val){
			var payTypeCd = $("input[name='pay_type_cd']:checked").val(); //상품납입기간 선택된 값 , 일시납 혹은 월납
			var discountTimesVal = val; //할인횟수
			var orginPayTimesVal = $("input[name='orginPayTimes']").val(); //납입횟수
			var pdtUnitPriceVal = $("input[name='pdtUnitPrice']").val();	//상품금액
			var monthPayAmountVal = $("input[name='monthPayAmount']").val();	//월납입액
			var times = 150;
			var payTimes, totalPayAmountVal;
		
			if(typeof payTypeCd != "undefined" && payTypeCd == "2"){ //월납일 경우
				if(discountTimesVal != "" && orginPayTimesVal != ""){
					payTimes = orginPayTimesVal - discountTimesVal; //가입납입기간 = 납입횟수 - 할인횟수
					$("input[name='payTimes']").val(payTimes);
				}else{
					$("input[name='payTimes']").val(0);
				}
				
				totalPayAmountVal = payTimes * monthPayAmountVal; //가입금액 = 가입납입기간 * 월납입액
				
				
			}else if(typeof payTypeCd != "undefined" && payTypeCd == "1"){ //일시납일 경우
				totalPayAmountVal = pdtUnitPriceVal - (discountTimesVal * (pdtUnitPriceVal/times));	//가입금액 =  상품금액 - {할인횟수 × (상품금액 ÷ 150)}
			}
			$("input[name='totalPayAmount']").val(totalPayAmountVal);
		}
	
		
	};

	auth.init();
});

//삼품 정보 리셋
function _pdtInfoReset(){
	$(".pdtInfo").find(".pdtName").html('');
	$(".pdtInfo").find(".pdtDescript").html('');
	$("input[name='pdtUnitPrice']").val('');
	$("input[name='strPdtUnitPrice']").val('');
	$(".payTypeCdMonth").html('').append('월납(N회)');
	$("input[name='orginPayTimes']").val('');
	$("input[name='strMonthPayAmount']").val('');
	$("input[name='monthPayAmount']").val('');
	$("#discountTimes").html('').append("<option value=''>선택</option>");
	$("input[name='payTimes']").val('');
	$("input[name='totalPayAmount']").val('');
}
