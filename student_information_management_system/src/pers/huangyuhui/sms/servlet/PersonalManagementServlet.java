package pers.huangyuhui.sms.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.huangyuhui.sms.dao.AdminDao;
import pers.huangyuhui.sms.dao.StudentDao;
import pers.huangyuhui.sms.dao.TeacherDao;
import pers.huangyuhui.sms.model.AdminInfo;
import pers.huangyuhui.sms.model.StudentInfo;
import pers.huangyuhui.sms.model.TeacherInfo;

@WebServlet("/PersonalManagementServlet")
public class PersonalManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static AdminDao adminDao = new AdminDao();
	private static TeacherDao teacherDao = new TeacherDao();
	private static StudentDao studentDao = new StudentDao();

	public PersonalManagementServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ������Ϊȫվ����ַ����������Ӵ ~
		response.setCharacterEncoding("utf-8");

		String method = request.getParameter("method");
		if ("toPersonalView".equals(method)) {
			toPersonalView(request, response);
		} else if ("toEditPasswod".equals(method)) {
			editPasswod(request, response);
			return;
		}
	}

	/**
	 * @Title: editPasswod
	 * @Description: �޸�����
	 * @param: request
	 * @param: response
	 * @return: void
	 * @throws IOException
	 */
	private void editPasswod(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// ��ȡ��¼�û�������
		int userType = Integer.valueOf(request.getSession().getAttribute("userType").toString());
		// ��ȡ����������Ϣ
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");

		// У��
		if (userType == 1) {// ����Ա
			AdminInfo adminInfo = new AdminInfo();
			adminInfo = (AdminInfo) request.getSession().getAttribute("userInfo");
			if (!adminInfo.getPassword().equals(oldPassword)) {
				response.getWriter().write("ԭ�������Ӵ !");
				return;
			} else {
				// �޸�
				if (adminDao.modifyPassword(adminInfo, newPassword)) {
					response.getWriter().write("success");
				} else {
					response.getWriter()
							.write("error: sorry ~ system error ! please check the database connection or try again !");
				}
			}

		} else if (userType == 2) {// ѧ��
			StudentInfo studentInfo = new StudentInfo();
			studentInfo = (StudentInfo) request.getSession().getAttribute("userInfo");
			if (!studentInfo.getPassword().equals(oldPassword)) {
				response.getWriter().write("ԭ�������Ӵ !");
				return;
			} else {
				if (studentDao.modifyPassword(studentInfo, newPassword)) {
					response.getWriter().write("success");
				} else {
					response.getWriter()
							.write("error: sorry ~ system error ! please check the database connection or try again !");
				}
			}

		} else if (userType == 3) {// ��ʦ
			TeacherInfo teacherInfo = new TeacherInfo();
			teacherInfo = (TeacherInfo) request.getSession().getAttribute("userInfo");
			if (!teacherInfo.getPassword().equals(oldPassword)) {
				response.getWriter().write("ԭ�������Ӵ !");
			} else {
				if (teacherDao.modifyPassword(teacherInfo, newPassword)) {
					response.getWriter().write("success");
				} else {
					response.getWriter()
							.write("error: sorry ~ system error ! please check the database connection or try again !");
				}
			}
		}
	}

	/**
	 * @Title: toPersonalView
	 * @Description: ������ת�����޸�����ҳ��
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void toPersonalView(HttpServletRequest request, HttpServletResponse response) {

		try {
			request.getRequestDispatcher("/WEB-INF/view/management/personalView.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
