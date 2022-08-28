document.write('<script src="/js/common/basic/sweetalert2@9.js"></script>');

const C_OK = 'success';
const C_FAIL = 'error';
const C_WARN = 'warning';
const C_INFO = 'info';
const C_QUEST = 'question';


function c_alert(title,icon,callback,text){
	Swal.fire({
	  title: title,
	  text: text,
	  icon: icon,
	  showCancelButton: false,
	  confirmButtonColor: '#3085d6',
	  cancelButtonColor: '#d33',
	  confirmButtonText: '확인',
	  cancelButtonText: '취소'
	}).then((result) => {
	  if (result.value) {
		if(typeof(callback) != 'undefined'){
			callback();
        }
	  }
	})	
}



function c_confirm(title,callback,text){
	Swal.fire({
	  title: title,
	  text: text,
	  icon: 'question',
	  showCancelButton: true,
	  confirmButtonColor: '#3085d6',
	  cancelButtonColor: '#d33',
	  confirmButtonText: '확인',
	  cancelButtonText: '취소'
	}).then((result) => {
	  if (result.value) {
          //"삭제" 버튼을 눌렀을 때 작업할 내용을 이곳에 넣어주면 된다. 
          if(typeof(callback) != 'undefined'){
			callback();
        }
	  }
	})	
}


function showLoading(){
    //화면의 높이와 너비를 구합니다.
    var maskHeight = $(document).height();
    var maskWidth  = window.document.body.clientWidth;

    //화면에 출력할 마스크를 설정해줍니다.
    var mask ="<div id='mask' style='position:absolute; z-index:1000; background-color:#000000; left:0; top:0;'></div>";

    //화면에 레이어 추가
    $('body')
    .append(mask)

    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채웁니다.
    $('#mask').css({
    'width' : maskWidth
    ,'height': maskHeight
    ,'opacity' :'0.3'
    });

    $(".page_loader").show();
}
function hideLoading(){
    $("#mask").remove();
    $(".page_loader").hide();
}