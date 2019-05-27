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
import pers.huangyuhui.sms.dao.TeacherDao;
import pers.huangyuhui.sms.model.Paging;
import pers.huangyuhui.sms.model.TeacherInfo;

@WebServlet("/TeacherManagementServlet")
public class TeacherManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static TeacherDao teacherDao = new TeacherDao();

	public TeacherManagementServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ������Ϊȫվ����ַ����������Ӵ ~
		response.setCharacterEncoding("utf-8");

		String method = request.getParameter("method");
		if ("toTeacherListView".equals(method)) {
			teacherListView(request, response);
		} else if ("addTeacher".equals(method)) {
			addTeacher(request, response);
		} else if ("getTeacherList".equals(method)) {
			getTeacherList(request, response);
		} else if ("editTeacher".equals(method)) {
			editTeacher(request, response);
		} else if ("deleteTeacher".equals(method)) {
			deleteTeacher(request, response);
		}
	}

	/**
	 * @Title: deleteTeacher
	 * @Description: ɾ����ʦ��Ϣ
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void deleteTeacher(HttpServletRequest request, HttpServletResponse response) {

		// ��ȡ��ɾ���Ľ�ʦid
		String[] idArray = request.getParameterValues("ids[]");

		// ƴ�ӽ�ʦid
		String idStr = "";
		for (String id : idArray) {
			idStr += id + ",";
		}
		idStr = idStr.substring(0, idStr.length() - 1);

		// ɾ������
		if (teacherDao.deleteTeacher(idStr)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: editTeacher
	 * @Description: �޸Ľ�ʦ��Ϣ
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void editTeacher(HttpServletRequest request, HttpServletResponse response) {

		// ��ȡ�޸���Ϣ
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		Integer id = Integer.valueOf(request.getParameter("id"));
		Integer classID = Integer.valueOf(request.getParameter("clazzid"));

		// �洢
		TeacherInfo teacherInfo = new TeacherInfo();
		teacherInfo.setId(id);
		teacherInfo.setName(name);
		teacherInfo.setSex(sex);
		teacherInfo.setEmail(email);
		teacherInfo.setMobile(phone);
		teacherInfo.setClassID(classID);

		// ��������
		if (teacherDao.editTeacherInfo(teacherInfo)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: getTeacherList
	 * @Description: ��ȡ��ʦ�б���Ϣ
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void getTeacherList(HttpServletRequest request, HttpServletResponse response) {

		// ( �û�Ȩ������ )��ȡ��ǰ��¼���û�����
		Integer userType = Integer.valueOf(request.getSession().getAttribute("userType").toString());

		// ��Ŀ�㷨��ֹ��page..���Ե�ҳ�淢����Ӧ����
		int currentPage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int pageSize = request.getParameter("rows") == null ? 999 : Integer.valueOf(request.getParameter("rows"));

		// ��ȡ�û���ѯ����
		String name = request.getParameter("teacherName");
		int classID = request.getParameter("clazzid") == null ? 0 : Integer.valueOf(request.getParameter("clazzid"));

		// �洢��ѯ����
		TeacherInfo teacherInfo = new TeacherInfo();
		teacherInfo.setId(0);
		teacherInfo.setName(name);
		teacherInfo.setClassID(classID);

		// �û�Ȩ������: �����ǰ�û�����Ϊ��ʦ,����Ȩ������Ϊ���ܲ�ѯ������Ϣ
		if (userType == 3) {
			TeacherInfo currentTeacherInfo = (TeacherInfo) request.getSession().getAttribute("userInfo");// ��ȡ��ǰ��¼�Ľ�ʦ��Ϣ
			teacherInfo.setId(currentTeacherInfo.getId());
		}

		// ��ȡ��ҳ��Ľ�ʦ�б���Ϣ
		List<TeacherInfo> teacherList = teacherDao.getTeacherList(teacherInfo, new Paging(currentPage, pageSize));

		// ��ȡ��ʦ��Ϣ�б������
		int totalNum = teacherDao.getTeacherListNum(teacherInfo);

		// ��Listת��ΪJSON����
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("total", totalNum);
		datas.put("rows", teacherList);
		try {
			// ��ҳ������ʾ��ʦ�б���Ϣ
			response.getWriter().write(JSONObject.fromObject(datas).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: addTeacher
	 * @Description: ��ӽ�ʦ��Ϣ
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void addTeacher(HttpServletRequest request, HttpServletResponse response) {

		// ��ȡ����ӵĽ�ʦ��Ϣ
		int classID = request.getParameter("clazzid") == null ? 0 : Integer.valueOf(request.getParameter("clazzid"));
		String tno = request.getParameter("tno");
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String sex = request.getParameter("sex");
		String email = request.getParameter("email");
		String mobile = request.getParameter("phone");

		// �洢
		TeacherInfo teacherInfo = new TeacherInfo();
		teacherInfo.setClassID(classID);
		teacherInfo.setTno(tno);
		teacherInfo.setName(name);
		teacherInfo.setPassword(password);
		teacherInfo.setSex(sex);
		teacherInfo.setEmail(email);
		teacherInfo.setMobile(mobile);

		// �������
		if (teacherDao.addTeacher(teacherInfo)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: teacherListView
	 * @Description: ������ת������ʦ��Ϣҳ��
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void teacherListView(HttpServletRequest request, HttpServletResponse response) {

		try {
			request.getRequestDispatcher("/WEB-INF/view/teacher/teacherList.jsp").forward(request, response);
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
