package com.tenminds.controller.contract;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.http.HttpStatus;
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
import com.tenminds.common.util.JsonUtil;
import com.tenminds.common.util.MoneyUtil;
import com.tenminds.common.util.PagingUtil;
import com.tenminds.common.util.StringUtil;
import com.tenminds.domain.cmn.channel.model.ChannelRecruitDto;
import com.tenminds.domain.cmn.code.entity.CodeC;
import com.tenminds.domain.cmn.code.model.CodeDto;
import com.tenminds.domain.common.pg.entity.BankId;
import com.tenminds.domain.common.pg.model.BankReponseDto;
import com.tenminds.domain.common.resolver.HttpHeader;
import com.tenminds.domain.common.security.LoginInfo;
import com.tenminds.domain.contract.model.ContractDto;
import com.tenminds.domain.contract.model.request.ContMRequest;
import com.tenminds.domain.contract.model.request.ContRegGrpRequest;
import com.tenminds.domain.contract.model.request.SrchContract;
import com.tenminds.domain.product.entity.PdtBrandC;
import com.tenminds.domain.product.model.ProductDto;
import com.tenminds.service.cmn.channel.ChannelService;
import com.tenminds.service.cmn.code.CodeService;
import com.tenminds.service.cmn.mgr.menu.MenuService;
import com.tenminds.service.common.pg.BankService;
import com.tenminds.service.contract.ContService;
import com.tenminds.service.product.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DY_LOGGER")
@RequiredArgsConstructor
@Controller
public class ContractController {

	private final ContService contService;

	private final MenuService menuService;

	private final ChannelService channelService;

	private final CodeService codeService;

	private final ProductService productService;
	
	private final BankService bankService;

	@GetMapping("/view/contract/current")
	public String authList(
			ModelMap model
			,@AuthenticationPrincipal LoginInfo loginInfo
			,HttpServletRequest request
            ,HttpHeader header
			) throws Exception {
		Map<String, Object> urlMap =  menuService.getUrlInfo(request.getRequestURI());
		model.addAttribute("urlInfo", urlMap);

		List<CodeC> frontPhoneNumberList = codeService.findAllByCatCd(12);
		model.addAttribute("frontPhoneNumberList", frontPhoneNumberList);

		model.addAttribute("today", StringUtil.dateToString(new Date(), "yyyy-MM-dd"));

		return "/view/contract/list";
	}


	@ResponseBody
	@GetMapping("/view/contract/current-paging")
	public Map<String, Object> authListPaging(
			@ModelAttribute SrchContract srchContM
			,ModelMap model
			,@AuthenticationPrincipal LoginInfo loginInfo
			,HttpServletRequest request
            ,HttpHeader header
			) throws Exception {
		Map<String, Object> map = new HashedMap<>();

		PagingUtil pagingDto = contService.selectContList(srchContM);

		map.put("pagingDto", pagingDto);
		return map;

	}

	@GetMapping("/view/contract/update")
	public String contractUpdate(
			ModelMap model
			,@ModelAttribute SrchContract srchContM
			,@AuthenticationPrincipal LoginInfo loginInfo
			,HttpServletRequest request
            ,HttpHeader header
			) throws Exception {
		Map<String, Object> urlMap =  menuService.getUrlInfo(request.getRequestURI());
		model.addAttribute("urlInfo", urlMap);

		ContractDto contractDto = contService.selectContractDetail(srchContM);

		List<PdtBrandC> brandList = productService.findAllBrand();

		List<ProductDto> productList = productService.findAllByBrandCd(contractDto.getBrand_cd());

		// 핸드폰 앞자리 리스트  12 : 핸드폰, 1 : 사용
		List<CodeDto> mpFrontList = codeService.codeListBycatCdByuseType(12,1);

		// 전화번호 앞자리 리스트 11 : 전화번호, 1 : 사용
		List<CodeDto> ctelFrontList = codeService.codeListBycatCdByuseType(11,1);

//		List<ContCustAgreeDto> custAgreeList = contService.selectContCustAgreeList(contractDto.getCustNo());

		model.addAttribute("brandList", brandList);
		model.addAttribute("productList", productList);
		model.addAttribute("contractDto", contractDto);
		model.addAttribute("mpFrontList", mpFrontList);
		model.addAttribute("ctelFrontList", ctelFrontList);
		model.addAttribute("today", StringUtil.dateToString(new Date(), "yyyy-MM-dd"));

		return "/view/contract/update";
	}

	@ResponseBody
	@GetMapping("/view/contract/cust-agree-list-ajax")
	public List<HashMap<String, Object>> contractCustAgreeListAjax(
			ModelMap model
			,@ModelAttribute SrchContract srchContM
			,@AuthenticationPrincipal LoginInfo loginInfo
			,HttpServletRequest request
            ,HttpHeader header
			) {
		List<HashMap<String, Object>> list = null;
		try {
			list  = contService.selectContCustAgreeList(srchContM.getSrchCustNo());
		} catch (Exception e) {
			log.debug("CONTRACT CUST AGREE ERROR [CUST_NO] : " + srchContM.getSrchCustNo() + " / [MESSAGE] " + e.toString());
		}
		return list;
	}

	/**********************************************************************************************/


	@GetMapping(value = "/view/test/contM-ajax")
	public String contCreateProcAjax2(@AuthenticationPrincipal LoginInfo loginInfo) throws Exception {
		Integer result = 0; //1:성공, 0:실패
		int accCnt = 2;
		ContRegGrpRequest contRegGrpRequest = new ContRegGrpRequest();
		String regMgrId = loginInfo.getManagerId(); //등록자 ID
		contRegGrpRequest.setRegMgrId(regMgrId); //set
		contRegGrpRequest.setInsuAccCnt(accCnt);

		ContMRequest contMRequest = new ContMRequest();
		contMRequest.setAgentOrdNo("222");
		contMRequest.setMonthPayAmount(1000);

		result = contService.contCreateProcAjax2(contRegGrpRequest, contMRequest, loginInfo);

		return "/view/cmn/auth/update";
	}



	/**
	 * 대리점에서 요청 등록 페이지(팝업)
	 * @param channelDto
	 * @return String
	 * @throws Exception
	 */
	@GetMapping(value = "/ext/contract/channel-pop")
	//orgin  public String contChannelValidChkCreate(@RequestBody ChannelDto channelDto, ModelMap model) throws Exception {
	public String contChannelValidChkCreate(@RequestParam String channelCd, @RequestParam String channelKey, @RequestParam String agentOrdNo, ModelMap model) throws Exception {
		LogMdc.name.accept("ext-channel-contract");
		try {
			Map<String, Object> returnMap = new HashMap<String, Object>();
			int result = 1; //결과값
			//orgin String channelCd = "";	//각 채널(대리점)코드
			//orgin String channelKey = ""; //각 채널(대리점)들의 key -> 사원번호, 사원ID 등
			//orgin String agentOrdNo = "";	//대리점 주문번호
			String isUse = "1"; //1:사용, 2: 미사용
			int catCd = 0;
			int useType = 1;// 1:사용(공통코드)


			//orgin if(channelDto != null) {

			//orgin channelCd = channelDto.getChannelCd();
			//orgin channelKey = channelDto.getChannelKey();

			//orgin }
//log.info("channelCd = " + channelCd);
//log.info("channelKey = " + channelKey);

log.info("channelCd = " + channelCd);
log.info("channelKey = " + channelKey);

			//1.채널코드 혹은 채널키가 없을 경우
			if(channelCd == null || channelCd.equals("") || channelKey == null || channelKey.equals("") || agentOrdNo == null || agentOrdNo.equals("")){
				return "redirect:/ext/error?code="+HttpStatus.BAD_REQUEST.value();
			}

			//2.채널(대리점) 리스트
			List<ChannelRecruitDto> channelList = channelService.cmnChannelListByIsuse(isUse);

			//3.채널(대리점) 모집자 리스트
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("channelCd", channelCd);
			paramMap.put("isUse", isUse);
			List<ChannelRecruitDto> channelMgrList = channelService.cmnChannelMgrList(paramMap);

			//4.휴대폰번호 앞자리 리스트
			catCd = 12; //휴대폰 앞자리
			List<CodeDto> mpList = codeService.codeListBycatCdByuseType(catCd, useType);

			//5.전화번호앞자리 리스트
			catCd = 11; //전화번호 앞자리
			List<CodeDto> telList = codeService.codeListBycatCdByuseType(catCd, useType);

			//6.해당 채널(대리점), 해당 채널키에 대한 해당 보험관리자의 모집자 코드 정보 조회
			HashMap<String, Object> paramMap2 = new HashMap<String, Object>();
			paramMap2.put("channelCd", channelCd);
			paramMap2.put("rmChannelKey", channelKey);
			paramMap2.put("isUse", isUse);
			ChannelRecruitDto crDto = channelService.cmnChannelMgrInfo(paramMap2);
			String recruitMgrCd = "";
			if(crDto != null){
				if(!crDto.getRecruitMgrCd().equals("")) {
					recruitMgrCd = crDto.getRecruitMgrCd();
				}
			}

			//7.브랜드 리스트
			List<PdtBrandC> brandList = productService.findAllBrand();
			
			//8.구좌수 리스트
			catCd = 19; //구좌수
			List<CodeDto> accCntList = codeService.codeListBycatCdByuseType(catCd, useType);
			
			//9.납입방법 리스트
			catCd = 20; //구좌수
			List<CodeDto> payTypeList = codeService.codeListBycatCdByuseType(catCd, useType);
			
			//10.은행사 리스트
			Integer pgCd = 18; //pg사 코드
			Integer pgIsUse = 1; //1:사용, 2:미사용
			
			List<BankReponseDto> bankList = bankService.bankListByPgCdIsUse(pgCd, pgIsUse);

			//add
			//orgin model.addAttribute("channelDto", channelDto);
			model.addAttribute("channelCd", channelCd);
			model.addAttribute("channelKey", channelKey);
			model.addAttribute("agentOrdNo", agentOrdNo);
			model.addAttribute("channelList", channelList);
			model.addAttribute("channelMgrList", channelMgrList);
			model.addAttribute("recruitMgrCd", recruitMgrCd);
			model.addAttribute("mpList", mpList);
			model.addAttribute("telList", telList);
			model.addAttribute("brandList", brandList);
			model.addAttribute("accCntList", accCntList);
			model.addAttribute("payTypeList", payTypeList);
			model.addAttribute("bankList", bankList);
			
		}catch (Exception e) {
			log.error("ContractController > contChannelValidChkCreate : " + e.toString());
		}

		return "/view/ext/contract/create";
	}//contChannelValidChkCreate end
	
	
	
	/**
	 * 브랜드 코드별 상품리스트
	 * @param brand_cd
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/ext/contract/productList-ajax", method = {RequestMethod.POST})
	@ResponseBody
	public String contProductListAjax(@RequestParam("brandCd") String brandCd, ModelMap model) throws Exception {
		LogMdc.name.accept("ext-channel-contract");
		String result = "";
		try {

			//브랜드 코드별 상품 리스트
			List<ProductDto> list = productService.findAllByBrandCd(brandCd);
			
			//to json 
			result = JsonUtil.objectToJson(list);
	
		} catch (Exception e) {
			// TODO: handle exception
			log.error("ContractController > contProductListAjax : " + e.toString());
		}		
		return result;
	}
	
	
	/**
	 * 상품정보 조회
	 * @param pdt_cd
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value = "/ext/contract/productInfo-ajax", method = {RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> contProductInfoAjax(@RequestParam("pdtCd") String pdtCd, ModelMap model) throws Exception {
		LogMdc.name.accept("ext-channel-contract");
		Map<String, Object> returnMap = null;
		String pdtName = "";
		String pdtDescript = "";
		int pdtUnitPrice = 0; //상품금액
		String strPdtUnitPrice = "";
		int payTimes = 0;//납입횟수
		int monthPayAmount = 0;
		String strMonthPayAmount = "";
		int discountNumMin = 0;
		int discountNumMax = 0;
		try {
			returnMap = new HashMap<String, Object>();
			
			
			//브랜드 코드별 상품 리스트
			ProductDto productDto = productService.findById(pdtCd);
			
			pdtName = productDto.getPdtName(); //상품명
			pdtDescript = productDto.getPdtDescript(); //상품설명
			pdtUnitPrice = productDto.getPdtUnitPrice(); //상품금액
			strPdtUnitPrice = MoneyUtil.getMoneyFmt(pdtUnitPrice); //금액 , 처리
			payTimes = productDto.getPayTimes();	//납입횟수
			monthPayAmount = productDto.getMonthPayAmount(); 	//월납입액
			strMonthPayAmount = MoneyUtil.getMoneyFmt(monthPayAmount); //금액 , 처리
			discountNumMin = productDto.getDiscountNumMin(); 	//할인횟수-최소
			discountNumMax = productDto.getDiscountNumMax();	//할인횟수-최대
			
			returnMap.put("pdtName", pdtName);
			returnMap.put("pdtDescript", pdtDescript);
			returnMap.put("pdtUnitPrice", pdtUnitPrice);
			returnMap.put("strPdtUnitPrice", strPdtUnitPrice);
			returnMap.put("payTimes", payTimes);
			returnMap.put("monthPayAmount", monthPayAmount);
			returnMap.put("strMonthPayAmount", strMonthPayAmount);
			returnMap.put("discountNumMin", discountNumMin);
			returnMap.put("discountNumMax", discountNumMax);
	
		} catch (Exception e) {
			// TODO: handle exception
			log.error("ContractController > contProductInfoAjax : " + e.toString());
		}		
		return returnMap;
	}
	

}
