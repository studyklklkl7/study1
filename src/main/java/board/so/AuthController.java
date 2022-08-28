package com.tenminds.controller.cmn.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tenminds.common.consts.LogMdc;
import com.tenminds.common.util.JpaPagingUtil;
import com.tenminds.common.util.PagingUtil;
import com.tenminds.domain.cmn.auth.entity.Auth;
import com.tenminds.domain.cmn.auth.model.AuthCnd;
import com.tenminds.domain.cmn.auth.model.AuthDto;
import com.tenminds.domain.cmn.code.model.CodeDto;
import com.tenminds.domain.common.resolver.HttpHeader;
import com.tenminds.domain.common.security.LoginInfo;
import com.tenminds.service.cmn.auth.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DY_LOGGER")
@RequiredArgsConstructor
@Controller
public class AuthController {


	private final int size = 3;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private JpaPagingUtil jpaPagingUtil;
	
	@GetMapping("/cmn/test/auth")
	public String mgr(@AuthenticationPrincipal LoginInfo loginInfo, HttpHeader header, ModelMap model, @PageableDefault(size = size, sort = "authCd", direction = Sort.Direction.DESC) Pageable pageable) throws Exception {	
		
		Page<Auth> list = authService.selectAuthList(pageable);
		
		String pagingHtml = jpaPagingUtil.JpaPagingUtil(list, size);

		model.addAttribute("list", list);
		model.addAttribute("pagingHtml", pagingHtml);
		/*
		 * model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
		 * model.addAttribute("next", pageable.next().getPageNumber());
		 * model.addAttribute("hasNext", list.hasNext()); model.addAttribute("hasPrev",
		 * list.hasPrevious());
		 */
		List<Auth> list2 = list.getContent();
		for(int i=0; i<list2.size(); i++){
			Auth aa	= (Auth)list2.get(i);
			//log.info(aa.getMgrInfoEntity().toString());
			
		}
		
		return "/view/cmn/auth/list";
	}
	
	@RequestMapping(value = "/view/cmn/auth/list",  method = {RequestMethod.POST,RequestMethod.GET})
	public String authListByState(@ModelAttribute("authCnd") AuthCnd authCnd, @AuthenticationPrincipal LoginInfo loginInfo, HttpHeader header, ModelMap model) throws Exception {	
		int catCd = 1;
		int useType = 1; //1:사용, 2:미사용
		int pageSize = 10; //페이지사이즈
		Long totalCnt = 0L;
		LogMdc.name.accept("Auth");
		authCnd.setSize(pageSize); //페이지사이즈 set
		authCnd.setSort(Sort.by(Sort.Order.desc("authCd"), Sort.Order.desc("authName"))); //정렬

		//코드리스트
		List<CodeDto> codeList = authService.codeListBycatCdByuseType(catCd, useType);
		
		//리스트
		PagingUtil pagingDto = authService.authListByState(authCnd);
		
		totalCnt = pagingDto.getTotal();
		
		model.addAttribute("codePg", pagingDto);		
		model.addAttribute("codeList", codeList);
		model.addAttribute("totalCnt", totalCnt);
		//model.addAttribute("srchStateCd", authCnd.getSrchStateCd());
		
		return "/view/cmn/auth/list";
	}
	
	
	@RequestMapping(value = "/view/cmn/auth/create",  method = {RequestMethod.POST,RequestMethod.GET})
	public String authCreate(@ModelAttribute("authCnd") AuthCnd authCnd, @AuthenticationPrincipal LoginInfo loginInfo, HttpHeader header, ModelMap model) throws Exception {	

		return "/view/cmn/auth/create";
	}
	
	@RequestMapping(value = "/view/cmn/auth/create-ajax", method = {RequestMethod.POST})
	@ResponseBody
	public String authCreateProcAjax(@ModelAttribute("authDto") AuthDto authDto, @AuthenticationPrincipal LoginInfo loginInfo, HttpHeader header, ModelMap model) throws Exception {	
		String result = "0"; //1:성공, 0:실패

		result = authService.authCreateProcAjax(authDto, loginInfo, header, model);

		return result;
	}
	
	
	@RequestMapping(value = "/view/cmn/auth/update",  method = {RequestMethod.POST})
	public String authUpdateByAuthCd(@RequestParam(value = "authCd") Integer authCd
			, @ModelAttribute("authDto") AuthDto authDto, @ModelAttribute("authCnd") AuthCnd authCnd, @AuthenticationPrincipal LoginInfo loginInfo, HttpHeader header, ModelMap model) throws Exception {	
		LogMdc.name.accept("Auth");
		int catCd = 1;
		int useType = 1; //1:사용, 2:미사용
		
		//Integer authCd = authDto.getAuthCd();
//log.info("authCd(업데이트 화면) ==" + authCd);
		
		//조회
		authDto = authService.authUpdateInfoByAuthCd(authCd);
		
		//코드리스트
		List<CodeDto> codeList = authService.codeListBycatCdByuseType(catCd, useType);
		
		model.addAttribute("authDto", authDto);
		model.addAttribute("codeList", codeList);

		return "/view/cmn/auth/update";
	}
	
	
	
	@RequestMapping(value = "/view/cmn/auth/update-ajax", method = {RequestMethod.PUT})
	@ResponseBody
	public String authUpdateProcAjax(@ModelAttribute("authDto") AuthDto authDto, @AuthenticationPrincipal LoginInfo loginInfo, HttpHeader header, ModelMap model) throws Exception {	
		String result = "0"; //1:성공, 0:실패

		result = authService.authUpdateProcAjax(authDto, loginInfo, header, model);

		return result;
	}
	
	/*
	 * @ResponseBody
	 * 
	 * @GetMapping("/view/system/mgr/mgr-paging") public PagingUtil
	 * getAllMgr(@ModelAttribute MgrCnd mgrCnd,ModelMap model) throws Exception{
	 * 
	 * model.addAttribute("mgrPg", mgrService.findAllMgr(mgrCnd));
	 * 
	 * return mgrService.findAllMgr(mgrCnd); }
	 * 
	 * 
	 * @GetMapping("/view/system/mgr/getMgrInfo-pop/{id}") public String getMgrInfo(
	 * 
	 * @PathVariable String id,
	 * 
	 * @AuthenticationPrincipal LoginInfo loginInfo ,HttpHeader header ,ModelMap
	 * model ) throws Exception {
	 * 
	 * List<MgrDto> mgrDto = mgrService.findMgrInfo(id);
	 * model.addAttribute("mgrDto", mgrDto);
	 * 
	 * return "/view/system/mgr/mgrUpd"; }
	 */
}
