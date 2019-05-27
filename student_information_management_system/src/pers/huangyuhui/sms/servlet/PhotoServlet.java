package pers.huangyuhui.sms.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;

import com.lizhou.exception.FileFormatException;
import com.lizhou.exception.NullFileException;
import com.lizhou.exception.ProtocolException;
import com.lizhou.exception.SizeException;
import com.lizhou.fileload.FileUpload;

//������~ �Զ���jar���� !

import pers.huangyuhui.sms.dao.StudentDao;
import pers.huangyuhui.sms.dao.TeacherDao;
import pers.huangyuhui.sms.model.StudentInfo;
import pers.huangyuhui.sms.model.TeacherInfo;

/**
 * @ClassName: PhotoServlet
 * @Description: �ٿ�ͼƬ
 * @author: HuangYuhui
 * @date: May 13, 2019 4:47:17 PM
 *
 */
@WebServlet("/PhotoServlet")
public class PhotoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static StudentDao studentDao = new StudentDao();
	private static TeacherDao teacherDao = new TeacherDao();

	public PhotoServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * ������ҪΪȫվ����ַ������� ~
		 */
		response.setCharacterEncoding("utf-8");

		String method = request.getParameter("method");
		if ("getPhoto".equals(method)) {
			getPhoto(request, response);
		} else if ("setPhoto".equals(method)) {
			uploadPhoto(request, response);
		}
	}

	/**
	 * @Title: setPhoto
	 * @Description: �ϴ��û�ͷ��( ����ϴ���ťʱ���� )
	 * @param: request
	 * @param: response
	 * @return: void
	 * @throws IOException
	 */
	private void uploadPhoto(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// ��ȡ��¼�û�������
		int sid = request.getParameter("sid") == null ? 0 : Integer.parseInt(request.getParameter("sid"));// ѧ��id
		int tid = request.getParameter("tid") == null ? 0 : Integer.parseInt(request.getParameter("tid"));// ��ʦid

		// �����ϴ�ͼƬ��ʽ����С
		FileUpload fileUpload = new FileUpload(request);
		fileUpload.setFileFormat("jpg");
		fileUpload.setFileFormat("png");
		fileUpload.setFileFormat("gif");
		fileUpload.setFileFormat("jpeg");
		fileUpload.setFileSize(2048);// 2M

		try {
			// ��ȡͼƬ
			InputStream uploadInputStream = fileUpload.getUploadInputStream();
			// ѧ��
			if (sid != 0) {
				StudentInfo studentInfo = new StudentInfo();
				studentInfo.setId(sid);
				studentInfo.setPhoto(uploadInputStream);
				// �ϴ�ͷ��
				if (studentDao.setStudentPhoto(studentInfo)) {
					response.getWriter().write("<div id='message'>ͷ���ϴ��ɹ��� ~</div>");
				} else {
					response.getWriter().write("<div id='message'>ͷ���ϴ�ʧ�� !</div>");
				}
			}
			// ��ʦ
			if (tid != 0) {
				TeacherInfo teacherInfo = new TeacherInfo();
				teacherInfo.setId(tid);
				teacherInfo.setPhoto(uploadInputStream);
				if (teacherDao.setTeacherPhoto(teacherInfo)) {
					response.getWriter().write("<div id='message'>ͷ���ϴ��ɹ��� ~</div>");
				} else {
					response.getWriter().write("<div id='message'>ͷ���ϴ�ʧ�� !</div>");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			response.getWriter().write("<div id='message'>ͼƬ��ȡʧ�� !</div>");
		} catch (ProtocolException e) {
			response.getWriter().write("<div id='message'>�ϴ�Э������� !</div>");
			e.printStackTrace();
		} catch (NullFileException e) {
			response.getWriter().write("<div id='message'>��ѡ��ָ����ͼƬӴ ~</div>");
			e.printStackTrace();
		} catch (SizeException e) {
			response.getWriter().write("<div id='message'>ͷ���С���ܳ���2MӴ !</div>");
			e.printStackTrace();
		} catch (FileFormatException e) {
			response.getWriter()
					.write("<div id='message'>����! ���ϴ�ͼƬѽ~ ���ʽΪ: " + fileUpload.getFileFormat() + " ���ļ�Ӵ ~</div>");
			e.printStackTrace();
		} catch (FileUploadException e) {
			response.getWriter().write("<div id='message'>ͼƬ�ϴ�ʧ�� !</div>");
			e.printStackTrace();
		}

	}

	/**
	 * @Title: getPhoto
	 * @Description: ��ȡͼƬ����ʾ��ҳ��
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	private void getPhoto(HttpServletRequest request, HttpServletResponse response) {

		// ��ȡ��¼�û�������
		int sid = request.getParameter("sid") == null ? 0 : Integer.parseInt(request.getParameter("sid"));// ѧ��id
		int tid = request.getParameter("tid") == null ? 0 : Integer.parseInt(request.getParameter("tid"));// ��ʦid

		/*
		 * �����û����Ͷ�ȡͷ��,��������ʾ��ҳ��
		 */
		if (sid != 0) {
			StudentInfo studentInfo = new StudentInfo();
			studentInfo = studentDao.getStudentInfoById(sid);
			if (studentInfo != null) {
				try (InputStream photoInputStream = studentInfo.getPhoto()) {

					if (photoInputStream != null) {
						byte[] b = new byte[photoInputStream.available()];
						photoInputStream.read(b);
						response.getOutputStream().write(b, 0, b.length);// ��ҳ������ʾͷ��
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		if (tid != 0) {
			TeacherInfo teacherInfo = new TeacherInfo();
			teacherInfo = teacherDao.getTeacherInfoById(tid);
			if (teacherInfo != null) {
				try (InputStream photoInputStream = teacherInfo.getPhoto()) {

					if (photoInputStream != null) {
						byte[] b = new byte[photoInputStream.available()];
						photoInputStream.read(b);
						response.getOutputStream().write(b, 0, b.length);
						return;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/*
		 * Ĭ�����( ��ͷ�� ): ��ȡĬ��ͷ�񲢽�����ʾ��ҳ��
		 */
		String path = request.getSession().getServletContext().getRealPath("");

		File file = new File(path + "resource\\image\\default_portrait.jpg");

		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			byte[] b = new byte[fileInputStream.available()];
			fileInputStream.read(b);

			response.getOutputStream().write(b, 0, b.length);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
