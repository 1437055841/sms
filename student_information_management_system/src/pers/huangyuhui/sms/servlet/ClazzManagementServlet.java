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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pers.huangyuhui.sms.dao.ClazzDao;
import pers.huangyuhui.sms.model.ClazzInfo;
import pers.huangyuhui.sms.model.Paging;

/**
 * @ClassName: ClazzManagementServlet
 * @Description: �༶��Ϣ����
 * @author: HuangYuhui
 * @date: May 11, 2019 1:23:58 PM
 * 
 */
@WebServlet("/ClazzManagementServlet")
public class ClazzManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static ClazzDao clazzDao = new ClazzDao();

	public ClazzManagementServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ������Ϊȫվ����ַ����������Ӵ ~
		response.setCharacterEncoding("utf-8");

		String method = request.getParameter("method");
		if ("toClassListView".equals(method)) {
			classListView(request, response);
		} else if ("getClassList".equals(method)) {
			getClassList(request, response);
		} else if ("addClass".equals(method)) {
			addClass(request, response);
		} else if ("deleteClass".equals(method)) {
			deleteClass(request, response);
		} else if ("editClass".equals(method)) {
			editClass(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * 
	 * @throws ServletException
	 * @Title: classList
	 * @Description: ������ת�����༶��Ϣҳ��
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void classListView(HttpServletRequest request, HttpServletResponse response) {

		try {
			request.getRequestDispatcher("/WEB-INF/view/class/classList.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: getClassList
	 * @Description:��ȡ�༶�б���Ϣ
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void getClassList(HttpServletRequest request, HttpServletResponse response) {

		// ��ȡ�û���ѯ����
		String className = request.getParameter("className");

		// ������Ŀ�㷨�����page..���Ե�ҳ����Ӧ��������( page,rows: ����classList.jsp�еı���������һ��Ӵ ~ )
		int currentPage = request.getParameter("page") == null ? 1 : Integer.valueOf(request.getParameter("page"));
		int pageSize = request.getParameter("rows") == null ? 999 : Integer.valueOf(request.getParameter("rows"));

		// ��ȡ��ҳ��İ༶�б�
		ClazzInfo clazzInfo = new ClazzInfo();
		clazzInfo.setName(className);
		List<ClazzInfo> clazzList = clazzDao.getClassList(clazzInfo, new Paging(currentPage, pageSize));

		// ��ȡ�༶�б������
		int totalNum = clazzDao.getClassListNum(clazzInfo);

		/*
		 * ��Listת��ΪJSON����
		 */
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("total", totalNum);// total��classList.jsp�еı������豣��һ��Ӵ ~
		datas.put("rows", clazzList);
		try {
			// ���ѧ����Ϣ����ҳ���а༶������������������ʾ����
			String fromStuInfoPage = request.getParameter("from");
			if ("combox".equals(fromStuInfoPage)) {
				response.getWriter().write(JSONArray.fromObject(clazzList).toString());
			} else {
				// �ڰ༶��Ϣ����ҳ������ʾ�༶�б���Ϣ
				response.getWriter().write(JSONObject.fromObject(datas).toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Title: addClass
	 * @Description:��Ӱ༶
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void addClass(HttpServletRequest request, HttpServletResponse response) {

		// ��ȡ�༶�������Ϣ
		String className = request.getParameter("name");
		String classIntroduce = request.getParameter("introduce");

		ClazzInfo clazzInfo = new ClazzInfo();
		clazzInfo.setName(className);
		clazzInfo.setIntroduce(classIntroduce);

		// �ж��Ƿ���ӳɹ�
		if (clazzDao.addClass(clazzInfo)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @Title: deleteClass
	 * @Description: ɾ���༶
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void deleteClass(HttpServletRequest request, HttpServletResponse response) {

		// ��ȡҪɾ���İ༶id
		int classId = Integer.valueOf(request.getParameter("classid"));

		// �ж��Ƿ�ɾ���ɹ�
		if (clazzDao.deleteClass(classId)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: editClass
	 * @Description: �޸İ༶��Ϣ
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void editClass(HttpServletRequest request, HttpServletResponse response) {

		// ��ȡ�޸�����
		String classId = request.getParameter("id");
		String name = request.getParameter("name");
		String introduce = request.getParameter("introduce");

		ClazzInfo clazzInfo = new ClazzInfo();
		clazzInfo.setId(Integer.valueOf(classId));
		clazzInfo.setName(name);
		clazzInfo.setIntroduce(introduce);

		// �ж��޸��Ƿ�ɹ�
		if (clazzDao.editClassInfo(clazzInfo)) {
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
