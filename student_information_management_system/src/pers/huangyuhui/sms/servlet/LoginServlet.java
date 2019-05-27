package pers.huangyuhui.sms.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import pers.huangyuhui.sms.dao.AdminDao;
import pers.huangyuhui.sms.dao.StudentDao;
import pers.huangyuhui.sms.dao.TeacherDao;
import pers.huangyuhui.sms.model.AdminInfo;
import pers.huangyuhui.sms.model.StudentInfo;
import pers.huangyuhui.sms.model.TeacherInfo;
import pers.huangyuhui.sms.util.StringUtil;

/**
 * @ClassName: VerifyLoginServlet
 * @Description: ��֤�û���¼
 * @author: HuangYuhui
 * @date: May 6, 2019 10:19:15 PM
 *
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static AdminDao adminDao = new AdminDao();
	private static TeacherDao teacherDao = new TeacherDao();
	private static StudentDao studentDao = new StudentDao();

	public LoginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String method = request.getParameter("method");

		if ("login".equals(method)) {
			login(request, response);
		} else if ("loginOut".equals(method)) {
			loginOut(request, response);
			return;
		}
	}

	/**
	 * @Title: login
	 * @Description: ��֤�û���¼����Ϣ
	 * @param: request
	 * @param: response
	 * @return: void
	 * @exception IOException
	 */
	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// ��ȡ�û���¼����
		int userType = Integer.valueOf(request.getParameter("userType"));
		// ��ȡ�û��ύ�ĵ�¼����Ϣ
		String name = request.getParameter("userName");
		String password = request.getParameter("userPassword");
		// ��ȡ��֤����Ϣ
		String verifiCode = request.getParameter("verificationCode");
		String verificationCode = request.getSession().getAttribute("verifiCode").toString();

		/*
		 * ��֤��֤��
		 */
		if (StringUtil.isEmpty(verifiCode) || !verifiCode.equalsIgnoreCase(verificationCode)) {
			response.getWriter().write("vcodeError");// ��֤�������ʾ
			return;
		}

		/*
		 * ��֤�û���¼
		 */
		switch (userType) {

		// ����Ա���
		case 1: {
			// ��֤�û���Ϣ
			AdminInfo adminInfo = new AdminInfo();
			adminInfo = adminDao.getUserInfo(name, password);
			if (adminInfo == null) {
				response.getWriter().write("loginError");
				return;
			}
			// ��¼�ɹ�: ���û���Ϣ�洢��Sesssion
			HttpSession session = request.getSession();
			session.setAttribute("userInfo", adminInfo);
			session.setAttribute("userType", userType);
			// ����ϵͳ��ҳ
			response.getWriter().write("loginSuccess");
			break;
		}
		// ѧ�����
		case 2: {
			StudentInfo studentInfo = new StudentInfo();
			studentInfo = studentDao.getUserInfo(name, password);
			if (studentInfo == null) {
				response.getWriter().print("loginError");
				return;
			}
			HttpSession session = request.getSession();
			session.setAttribute("userInfo", studentInfo);
			session.setAttribute("userType", userType);
			response.getWriter().print("loginSuccess");
			break;
		}

		// ��ʦ���
		case 3: {
			TeacherInfo teacherInfo = new TeacherInfo();
			teacherInfo = teacherDao.getUserInfo(name, password);
			if (teacherInfo == null) {
				response.getWriter().print("loginError");
				return;
			}
			HttpSession session = request.getSession();
			session.setAttribute("userInfo", teacherInfo);
			session.setAttribute("userType", userType);
			response.getWriter().print("loginSuccess");
			break;
		}

		}

	}

	/**
	 * @throws IOException
	 * @Title: loginOut
	 * @Description: ע���û���Ϣ
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void loginOut(HttpServletRequest request, HttpServletResponse response) {
		request.getSession().removeAttribute("userInfo");
		request.getSession().removeAttribute("userType");

		try {
			response.sendRedirect("index.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
