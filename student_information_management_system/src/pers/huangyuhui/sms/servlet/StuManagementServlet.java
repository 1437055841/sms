package pers.huangyuhui.sms.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import pers.huangyuhui.sms.dao.StudentDao;
import pers.huangyuhui.sms.model.Paging;
import pers.huangyuhui.sms.model.StudentInfo;

/**
 * @ClassName: StuManagementServlet
 * @Description: �ٿ�ѧ����Ϣ
 * @author: HuangYuhui
 * @date: May 11, 2019 12:31:36 PM
 *
 */
@WebServlet("/StuManagementServlet")
public class StuManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static StudentDao studentDao = new StudentDao();

	public StuManagementServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ������Ϊȫվ����ַ����������Ӵ ~
		response.setCharacterEncoding("utf-8");

		String method = request.getParameter("method");
		if ("toStudentListView".equals(method)) {
			studentListView(request, response);
		} else if ("addStudent".equals(method)) {
			addStudent(request, response);
		} else if ("getStudentList".equals(method)) {
			getStudentList(request, response);
		} else if ("editStudent".equals(method)) {
			editStudent(request, response);
		} else if ("deleteStudent".equals(method)) {
			deleteStudent(request, response);
		}
	}

	/**
	 * @Title: deleteStudent
	 * @Description: ɾ��ѧ����Ϣ
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) {

		// ��ȡҪɾ����ѧ��id
		String[] idArray = request.getParameterValues("ids[]");

		// ƴ��ѧ��id
		String idStr = "";
		for (String id : idArray) {
			idStr += id + ",";
		}
		idStr = idStr.substring(0, idStr.length() - 1);

		// ɾ������
		if (studentDao.deleteStudent(idStr)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @Title: editStudent
	 * @Description: �޸�ѧ����Ϣ
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void editStudent(HttpServletRequest request, HttpServletResponse response) {

		// ��ȡ�޸���Ϣ
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		Integer id = Integer.valueOf(request.getParameter("id"));
		Integer classID = Integer.valueOf(request.getParameter("clazzid"));

		// �洢
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setId(id);
		studentInfo.setName(name);
		studentInfo.setSex(sex);
		studentInfo.setEmail(email);
		studentInfo.setMobile(phone);
		studentInfo.setClassID(classID);

		// ��������
		if (studentDao.editStudentInfo(studentInfo)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @Title: getStudentList
	 * @Description: ��ȡ�༶�б���Ϣ
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void getStudentList(HttpServletRequest request, HttpServletResponse response) {

		// ( �û�Ȩ������ )��ȡ��ǰ��¼���û�����
		Integer userType = Integer.valueOf(request.getSession().getAttribute("userType").toString());

		// ��Ŀ�����ֹ��page..���Ե�ҳ�淢����Ӧ����
		int currentPage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int pageSize = request.getParameter("rows") == null ? 999 : Integer.valueOf(request.getParameter("rows"));

		// ��ȡ�û���ѯ����
		String name = request.getParameter("studentName");
		int classID = request.getParameter("clazzid") == null ? 0 : Integer.valueOf(request.getParameter("clazzid"));

		// �洢��ѯ����
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setId(0);
		studentInfo.setName(name);
		studentInfo.setClassID(classID);

		// �û�Ȩ������: �����ǰ�û�����Ϊѧ��,����Ȩ������Ϊ���ܲ�ѯ������Ϣ
		if (userType == 2) {
			StudentInfo currentStudentInfo = (StudentInfo) request.getSession().getAttribute("userInfo");// ��ȡ��ǰ��¼��ѧ����Ϣ
			studentInfo.setId(currentStudentInfo.getId());
		}

		// ��ȡ��ҳ���ѧ���б���Ϣ
		List<StudentInfo> studentList = studentDao.getStudentList(studentInfo, new Paging(currentPage, pageSize));

		// ��ȡѧ����Ϣ�б������
		int totalNum = studentDao.getStudentListNum(studentInfo);

		// ��Listת��ΪJSON����
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("total", totalNum);
		datas.put("rows", studentList);
		try {
			// ��ҳ������ʾѧ���б���Ϣ
			response.getWriter().write(JSONObject.fromObject(datas).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: studentListView
	 * @Description: ������ת����ѧ����Ϣҳ��
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void studentListView(HttpServletRequest request, HttpServletResponse response) {

		try {
			request.getRequestDispatcher("/WEB-INF/view/student/studentList.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: studentList
	 * @Description: ���ѧ����Ϣ
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void addStudent(HttpServletRequest request, HttpServletResponse response) {

		// ��ȡ��ӵ�ѧ����Ϣ
		int classID = request.getParameter("clazzid") == null ? 0 : Integer.valueOf(request.getParameter("clazzid"));
		String sno = request.getParameter("sno");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String sex = request.getParameter("sex");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");

		// �洢
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setClassID(classID);
		studentInfo.setSno(sno);
		studentInfo.setName(name);
		studentInfo.setPassword(password);
		studentInfo.setSex(sex);
		studentInfo.setEmail(email);
		studentInfo.setMobile(phone);

		// �������
		if (studentDao.addStudent(studentInfo)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
