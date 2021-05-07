///**
// *
// */
//package com.abnamro.nl.channels.geninfo.bankmail.asc.interfaces;
//
//import static org.easymock.EasyMock.createNiceMock;
//import static org.easymock.EasyMock.expect;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.log4j.BasicConfigurator;
//import org.easymock.EasyMock;
//
//import static org.easymock.EasyMock.*;
//import org.junit.Before;
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//import com.abnamro.genj.generic.SecurityContext;
//import com.abnamro.nl.channels.geninfo.bankmail.interfaces.BankmailApplicationException;
//import com.abnamro.nl.configurationservice.ConfigurationService;
//import com.abnamro.nl.configurationservice.ConfigurationServiceImpl;
//import com.abnamro.nl.employeeinfo.service.interfaces.AccountManagerEmployeeDTO;
//import com.abnamro.nl.employeeinfo.service.interfaces.EmployeeInfoService;
//import com.abnamro.nl.employeeinfo.service.interfaces.EmployeeInfoServiceException;
//import com.siebel.customui.BankmailAccess;
//import com.siebel.customui.BankmailPlus1Input;
//import com.siebel.customui.BankmailPlus1Output;
//import com.siebel.xml.aabr_20bankmail_20response_20io.AccessGrant;
//import com.siebel.xml.aabr_20bankmail_20response_20io.ListOfBankmailResponse;
//import com.siebel.xml.aabr_20bankmail_20response_20io.ReturnCodes;
//import com.siebel.xml.aabr_20bankmail_20response_20io.Users;
//
///**
// * @author C29871
// */
//public class CCAMailboxDelegateTest {
//
//	private EmployeeInfoService employeeInfoService = null;
//	private AccountManagerEmployeeDTO accountManagerEmployeeDTO = new AccountManagerEmployeeDTO();
//	private BankmailPlus1Output bankmailPlus1Output = new BankmailPlus1Output();
//	private SecurityContext securityContext = null;
//	private String mailboxName = "cc98894413";
//	String userRef = "XN1917";
//	public static final int SUBSTR_THIRD_LETTER = 2;
//	String cCA = null;
//	ConfigurationService configService = null;
//	ConfigurationServiceImpl configurationService;
//	BankmailAccess mockedAccess;
//	CCAMailboxTemplate cCAMailboxTemplate;
//
//	/**
//	 * initialize
//	 */
//	@Before
//	public void initialize() {
//		BasicConfigurator.configure();
//		employeeInfoService = createNiceMock(EmployeeInfoService.class);
//		securityContext = createNiceMock(SecurityContext.class);
//		configService = createNiceMock(ConfigurationService.class);
//		mockedAccess = createNiceMock(BankmailAccess.class);
//		cCAMailboxTemplate = createMock(CCAMailboxTemplate.class);
//	}
//
//	/**
//	 * testForPrivateEmpGetMailBoxSuccess
//	 */
//	@Test
//	public void testForPrivateEmpGetMailBoxSuccess() {
//		try {
//			String cca = "98894413";
//			expect(securityContext.getUserReference()).andReturn(userRef).anyTimes();
//			setEmployeeInfoDetails(accountManagerEmployeeDTO);
//			CCAMailboxDelegate ccAMailboxDelegate = new CCAMailboxDelegate();
//			ccAMailboxDelegate.setEmployeeInfoService(employeeInfoService);
//			ccAMailboxDelegate.setBankmailAccessImpl(mockedAccess);
//			ccAMailboxDelegate.setMailboxTemplate(cCAMailboxTemplate);
//			expect(employeeInfoService.retrieveAccountManagerInfoByCCA(cca, false)).andReturn(accountManagerEmployeeDTO)
//				.anyTimes();
//			setOutputForGetmailBox(bankmailPlus1Output);
//			expect(cCAMailboxTemplate.getMailboxName(cca)).andReturn(mailboxName).anyTimes();
//			expect(cCAMailboxTemplate.getSignatureTemplate()).andReturn(
//				"\n\nMet vriendelijke groet, \nABN AMRO MeesPierson \n\n#$EmployeeName$#").anyTimes();
//			expect(cCAMailboxTemplate.getDisplayNamePrefix()).andReturn("SHUBH").anyTimes();
//			expect(cCAMailboxTemplate.isCCAMailboxName(mailboxName)).andReturn(true).anyTimes();
//			expect(mockedAccess.bankmailPlus1(EasyMock.anyObject(BankmailPlus1Input.class)))
//				.andReturn(bankmailPlus1Output);
//			EasyMock.replay(mockedAccess, securityContext, employeeInfoService, cCAMailboxTemplate);
//			assertEquals(ccAMailboxDelegate.getMailbox(securityContext, mailboxName).getDisplayName(),
//				accountManagerEmployeeDTO.getName());
//		} catch (EmployeeInfoServiceException e) {
//			e.printStackTrace();
//		} catch (BankmailApplicationException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 * @param bankmailPlus1Output bankmailPlus1Output
//	 */
//	private void setOutputForGetmailBox(BankmailPlus1Output bankmailPlus1Output) {
//
//		ReturnCodes returnCodes = new ReturnCodes();
//		returnCodes.setReturnCode("0");
//		Users users = new Users();
//		users.setRecExists("Y");
//		users.setUSERSBTID(userRef);
//		users.getReturnCodes().add(returnCodes);
//		ListOfBankmailResponse listOfbankmailResp = new ListOfBankmailResponse();
//		listOfbankmailResp.getUsers().add(users);
//		bankmailPlus1Output.setListOfBankmailResponse(listOfbankmailResp);
//	}
//
//	/**
//	 * testForPrivateEmpGetMailBoxSuccess
//	 */
//	@Test
//	public void testForPrefferedEmpGetMailBoxSuccess() {
//		try {
//			String cca = "98894413";
//			expect(securityContext.getUserReference()).andReturn(userRef).anyTimes();
//			setPrefferedEmpDetails(accountManagerEmployeeDTO);
//			CCAMailboxDelegate ccAMailboxDelegate = new CCAMailboxDelegate();
//			ccAMailboxDelegate.setEmployeeInfoService(employeeInfoService);
//			ccAMailboxDelegate.setBankmailAccessImpl(mockedAccess);
//			ccAMailboxDelegate.setMailboxTemplate(cCAMailboxTemplate);
//			expect(employeeInfoService.retrieveAccountManagerInfoByCCA(cca, false)).andReturn(accountManagerEmployeeDTO)
//				.anyTimes();
//			setOutputForGetmailBox(bankmailPlus1Output);
//			expect(cCAMailboxTemplate.getMailboxName(cca)).andReturn(mailboxName).anyTimes();
//			expect(cCAMailboxTemplate.getSignatureTemplate()).andReturn(
//				"\n\nMet vriendelijke groet, \nABN AMRO MeesPierson \n\n#$EmployeeName$#").anyTimes();
//			expect(cCAMailboxTemplate.getDisplayNamePrefix()).andReturn("SNEHAL").anyTimes();
//			expect(cCAMailboxTemplate.isCCAMailboxName(mailboxName)).andReturn(true).anyTimes();
//			expect(mockedAccess.bankmailPlus1(EasyMock.anyObject(BankmailPlus1Input.class)))
//				.andReturn(bankmailPlus1Output);
//			EasyMock.replay(mockedAccess, securityContext, employeeInfoService, cCAMailboxTemplate);
//			assertEquals(ccAMailboxDelegate.getMailbox(securityContext, mailboxName).getDisplayName(),
//				accountManagerEmployeeDTO.getName());
//		} catch (EmployeeInfoServiceException e) {
//			e.printStackTrace();
//		} catch (BankmailApplicationException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 *
//	 */
//	private void setPrefferedEmpDetails(AccountManagerEmployeeDTO accountManagerEmployeeDTO) {
//		accountManagerEmployeeDTO.setCCA("98894413");
//		accountManagerEmployeeDTO.setName("SNEHAL");
//
//	}
//
//	/**
//	 * @param accountManagerEmployeeDTO accountManagerEmployeeDTO
//	 */
//	private void setEmployeeInfoDetails(AccountManagerEmployeeDTO accountManagerEmployeeDTO) {
//		accountManagerEmployeeDTO.setCCA("98894413");
//		accountManagerEmployeeDTO.setName("SHUBH");
//
//	}
//
//	/**
//	 * testGetMailBoxesSuccessScenario
//	 */
//	@Test
//	public void testGetMailBoxesSuccessScenario() {
//		try {
//			String cca = "98894413";
//			expect(securityContext.getUserReference()).andReturn(userRef).anyTimes();
//			setEmployeeInfoDetails(accountManagerEmployeeDTO);
//
//			CCAMailboxDelegate ccAMailboxDelegate = new CCAMailboxDelegate();
//			ccAMailboxDelegate.setEmployeeInfoService(employeeInfoService);
//			ccAMailboxDelegate.setBankmailAccessImpl(mockedAccess);
//			ccAMailboxDelegate.setMailboxTemplate(cCAMailboxTemplate);
//
//			expect(employeeInfoService.retrieveAccountManagerEmployeeBySBTId(anyString())).andReturn(
//				accountManagerEmployeeDTO);
//			setOutputForGetmailBoxes(bankmailPlus1Output);
//			expect(cCAMailboxTemplate.isCCAMailboxName(mailboxName)).andReturn(true);
//			expect(mockedAccess.bankmailPlus1(EasyMock.anyObject(BankmailPlus1Input.class)))
//				.andReturn(bankmailPlus1Output);
//			expect(cCAMailboxTemplate.getMailboxName(cca)).andReturn("cc98894413").anyTimes();
//			expect(cCAMailboxTemplate.getSignatureTemplate()).andReturn(
//				"\n\nMet vriendelijke groet, \nABN AMRO MeesPierson \n\n#$EmployeeName$#").anyTimes();
//			expect(cCAMailboxTemplate.getDisplayNamePrefix()).andReturn("SHUBH").anyTimes();
//			expect(cCAMailboxTemplate.isCCAMailboxName(mailboxName)).andReturn(true).anyTimes();
//			EasyMock.replay(securityContext, employeeInfoService, mockedAccess, cCAMailboxTemplate);
//
//			assertEquals(ccAMailboxDelegate.getMailboxes(securityContext).get(0).getDisplayName(), accountManagerEmployeeDTO
//				.getName());
//
//		} catch (BankmailApplicationException e) {
//			e.printStackTrace();
//		} catch (EmployeeInfoServiceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * @param bankPlus1Output bankPlus1Output
//	 */
//	private void setOutputForGetmailBoxes(BankmailPlus1Output bankPlus1Output) {
//		bankmailPlus1Output = new BankmailPlus1Output();
//		ReturnCodes returnCodes = new ReturnCodes();
//		returnCodes.setReturnCode("0");
//		AccessGrant accessGrant = new AccessGrant();
//		accessGrant.setSBTACCESSID("BO9928");
//		Users users = new Users();
//		users.setUSERSBTID(userRef);
//		users.getReturnCodes().add(returnCodes);
//		users.getAccessGrant().add(accessGrant);
//		ListOfBankmailResponse listOfbankmailResp = new ListOfBankmailResponse();
//		listOfbankmailResp.getUsers().add(users);
//		bankmailPlus1Output.setListOfBankmailResponse(listOfbankmailResp);
//	}
//
//	/**
//	 * testGetMailBoxErrorScenario
//	 */
//	@Test
//	public void testGetMailBoxErrorScenario() {
//		try {
//			expect(securityContext.getUserReference()).andReturn(userRef).anyTimes();
//			setEmployeeInfoDetails(accountManagerEmployeeDTO);
//			CCAMailboxDelegate ccAMailboxDelegate = new CCAMailboxDelegate();
//			ccAMailboxDelegate.setMailboxTemplate(cCAMailboxTemplate);
//			expect(employeeInfoService.retrieveAccountManagerEmployeeBySBTId(anyString())).andReturn(
//				accountManagerEmployeeDTO);
//			setOutputDetailsForGetMailboxError(bankmailPlus1Output);
//			expect(mockedAccess.bankmailPlus1(EasyMock.anyObject(BankmailPlus1Input.class)))
//				.andReturn(bankmailPlus1Output).anyTimes();
//			expect(cCAMailboxTemplate.isCCAMailboxName(mailboxName)).andReturn(true);
//			EasyMock.replay(mockedAccess, employeeInfoService, securityContext, cCAMailboxTemplate);
//			ccAMailboxDelegate.setEmployeeInfoService(employeeInfoService);
//			ccAMailboxDelegate.setBankmailAccessImpl(mockedAccess);
//			assertEquals(ccAMailboxDelegate.getMailbox(securityContext, mailboxName).getMessages().getMessages().get(0)
//				.getMessageKey(), "MESSAGE_BAI509_5007");
//		} catch (EmployeeInfoServiceException e) {
//			e.printStackTrace();
//		} catch (BankmailApplicationException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * @param bankmailPlus1Output bankmailPlus1Output
//	 */
//	private void setOutputDetailsForGetMailboxError(BankmailPlus1Output bankmailPlus1Output) {
//		ReturnCodes returnCodes = new ReturnCodes();
//		returnCodes.setReturnCode("0");
//		Users users = new Users();
//		users.setRecExists("N");
//		users.setUSERSBTID(userRef);
//		users.getReturnCodes().add(returnCodes);
//		ListOfBankmailResponse listOfbankmailResp = new ListOfBankmailResponse();
//		listOfbankmailResp.getUsers().add(users);
//		bankmailPlus1Output.setListOfBankmailResponse(listOfbankmailResp);
//	}
//
//	/**
//	 * testGetMailBoxErrorScenarioSecondCase
//	 */
//	@Test
//	public void testGetMailBoxErrorScenarioSecondCase() {
//		try {
//			expect(securityContext.getUserReference()).andReturn(userRef).anyTimes();
//			setEmployeeInfoDetails(accountManagerEmployeeDTO);
//			CCAMailboxDelegate ccAMailboxDelegate = new CCAMailboxDelegate();
//			ccAMailboxDelegate.setMailboxTemplate(cCAMailboxTemplate);
//			expect(employeeInfoService.retrieveAccountManagerEmployeeBySBTId(anyString())).andReturn(
//				accountManagerEmployeeDTO);
//			// final BankmailAccess BANKMAIL_ACCESS = new BankmailAccessImpl(mockedAccess);
//			setOutputDetailsForGetMailboxErrorSecondCase(bankmailPlus1Output);
//			expect(mockedAccess.bankmailPlus1(EasyMock.anyObject(BankmailPlus1Input.class)))
//				.andReturn(bankmailPlus1Output).anyTimes();
//			expect(cCAMailboxTemplate.isCCAMailboxName(mailboxName)).andReturn(true).anyTimes();
//			EasyMock.replay(mockedAccess, employeeInfoService, securityContext, cCAMailboxTemplate);
//			ccAMailboxDelegate.setEmployeeInfoService(employeeInfoService);
//			ccAMailboxDelegate.setBankmailAccessImpl(mockedAccess);
//			assertEquals(ccAMailboxDelegate.getMailbox(securityContext, mailboxName).getMessages().getMessages().get(0)
//				.getMessageKey(), "MESSAGE_BAI509_5007");
//		} catch (EmployeeInfoServiceException e) {
//			e.printStackTrace();
//		} catch (BankmailApplicationException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * @param bankmailPlus1Output bankmailPlus1Output
//	 */
//	private void setOutputDetailsForGetMailboxErrorSecondCase(BankmailPlus1Output bankmailPlus1Output) {
//		ReturnCodes returnCodes = new ReturnCodes();
//		returnCodes.setReturnCode("1");
//		returnCodes.setErrorMessage("ERROR");
//		Users users = new Users();
//		users.setUSERSBTID(userRef);
//		users.getReturnCodes().add(returnCodes);
//		ListOfBankmailResponse listOfbankmailResp = new ListOfBankmailResponse();
//		listOfbankmailResp.getUsers().add(users);
//		bankmailPlus1Output.setListOfBankmailResponse(listOfbankmailResp);
//	}
//
//	/**
//	 * testGetMailBoxesErrorScenario
//	 */
//	@Test
//	public void testGetMailBoxesErrorScenario() {
//		List<String> sBTIdList = new ArrayList<String>();
//		try {
//			setEmployeeInfoDetails(accountManagerEmployeeDTO);
//			expect(securityContext.getUserReference()).andReturn(userRef);
//			expect(employeeInfoService.retrieveAccountManagerEmployeeBySBTId(anyString())).andReturn(
//				accountManagerEmployeeDTO);
//			sBTIdList.add(userRef);
//
//			setOutputDetailsForGetMailboxesError(bankmailPlus1Output);
//			expect(mockedAccess.bankmailPlus1(EasyMock.anyObject(BankmailPlus1Input.class)))
//				.andReturn(bankmailPlus1Output);
//			expect(cCAMailboxTemplate.isCCAMailboxName(mailboxName)).andReturn(true).anyTimes();
//			EasyMock.replay(securityContext, employeeInfoService, mockedAccess, cCAMailboxTemplate);
//
//			CCAMailboxDelegate ccAMailboxDelegate = new CCAMailboxDelegate();
//			ccAMailboxDelegate.setEmployeeInfoService(employeeInfoService);
//			ccAMailboxDelegate.setBankmailAccessImpl(mockedAccess);
//			ccAMailboxDelegate.setMailboxTemplate(cCAMailboxTemplate);
//			assertEquals(ccAMailboxDelegate.getMailboxes(securityContext).get(0).getMessages().getMessages().get(0)
//				.getMessageKey(), "MESSAGE_BAI509_5037");
//
//		} catch (BankmailApplicationException e) {
//			e.printStackTrace();
//		} catch (EmployeeInfoServiceException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * @param bankmailPlus1Output bankmailPlus1Output
//	 */
//	private void setOutputDetailsForGetMailboxesError(BankmailPlus1Output bankmailPlus1Output) {
//		ReturnCodes returnCodes = new ReturnCodes();
//		returnCodes.setReturnCode("1");
//		Users users = new Users();
//		users.getReturnCodes().add(returnCodes);
//		ListOfBankmailResponse listOfbankmailResp = new ListOfBankmailResponse();
//		listOfbankmailResp.getUsers().add(users);
//		bankmailPlus1Output.setErrorSpcCode("ERR_USER_002");
//		bankmailPlus1Output.setListOfBankmailResponse(listOfbankmailResp);
//
//	}
//
//	/**
//	 * testGetMailboxesForMultipleSBTIds
//	 */
//	@Test
//	public void testGetMailboxesForMultipleSBTIds() {
//		try {
//			String cca = "98894413";
//			expect(securityContext.getUserReference()).andReturn(userRef).anyTimes();
//			setEmployeeInfoDetails(accountManagerEmployeeDTO);
//			CCAMailboxDelegate ccAMailboxDelegate = new CCAMailboxDelegate();
//			ccAMailboxDelegate.setEmployeeInfoService(employeeInfoService);
//			ccAMailboxDelegate.setBankmailAccessImpl(mockedAccess);
//			ccAMailboxDelegate.setMailboxTemplate(cCAMailboxTemplate);
//			expect(employeeInfoService.retrieveAccountManagerEmployeeBySBTId(anyString())).andReturn(
//				accountManagerEmployeeDTO);
//			setOutputForGetmailboxesMultipleSBTIds(bankmailPlus1Output);
//			expect(mockedAccess.bankmailPlus1(EasyMock.anyObject(BankmailPlus1Input.class)))
//				.andReturn(bankmailPlus1Output);
//			expect(cCAMailboxTemplate.getMailboxName(cca)).andReturn("cc98894413").anyTimes();
//			expect(cCAMailboxTemplate.getSignatureTemplate()).andReturn(
//				"\n\nMet vriendelijke groet, \nABN AMRO MeesPierson \n\n#$EmployeeName$#").anyTimes();
//			expect(cCAMailboxTemplate.getDisplayNamePrefix()).andReturn("SHUBH").anyTimes();
//			expect(cCAMailboxTemplate.isCCAMailboxName(mailboxName)).andReturn(true).anyTimes();
//			EasyMock.replay(mockedAccess, securityContext, employeeInfoService, cCAMailboxTemplate);
//
//			assertEquals(ccAMailboxDelegate.getMailboxes(securityContext).get(0).getDisplayName(), accountManagerEmployeeDTO
//				.getName());
//		} catch (EmployeeInfoServiceException e) {
//			e.printStackTrace();
//		} catch (BankmailApplicationException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 * @param bankmailPlus1Output bankmailPlus1Output
//	 */
//	private void setOutputForGetmailboxesMultipleSBTIds(BankmailPlus1Output bankmailPlus1Output) {
//		ReturnCodes returnCodes = new ReturnCodes();
//		returnCodes.setReturnCode("0");
//		Users users = null;
//		AccessGrant sbtId1 = new AccessGrant();
//		sbtId1.setSBTACCESSID("TR0820");
//		AccessGrant sbtId2 = new AccessGrant();
//		sbtId1.setSBTACCESSID("VE8986");
//		users = new Users();
//		users.setRecExists("Y");
//		users.setUSERSBTID(userRef);
//		users.getAccessGrant().add(sbtId2);
//		users.getAccessGrant().add(sbtId1);
//		users.getReturnCodes().add(returnCodes);
//		ListOfBankmailResponse listOfbankmailResp = new ListOfBankmailResponse();
//		listOfbankmailResp.getUsers().add(users);
//		bankmailPlus1Output.setListOfBankmailResponse(listOfbankmailResp);
//	}
//}
