package pers.huangyuhui.sms.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pers.huangyuhui.sms.util.CreateVerifiCodeImage;

/**
 * @ClassName: VerificationCodeServlet
 * @Description: �ٿ���֤��
 * @author: HuangYuhui
 * @date: May 6, 2019 6:49:44 PM
 *
 */
@WebServlet("/OutVerifiCodeServlet")
public class OutVerifiCodeServlet extends HttpServlet {

	private String verifiCode;// ��֤��
	private BufferedImage verifiCodeImage;// ��֤��ͼƬ
	private static final long serialVersionUID = 1L;

	public OutVerifiCodeServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String method = request.getParameter("method");
		if ("loginVerifiCode".equals(method)) {
			generateLoginVerifiCode(request, response);
			return;
		}
	}

	/**
	 * @Title: generateLoginVerifiCode
	 * @Description: ������֤��ͼƬ
	 * @param: request
	 * @param: response
	 * @return: void
	 */
	protected void generateLoginVerifiCode(HttpServletRequest request, HttpServletResponse response) {

		// ��ȡ��֤����Ϣ
		verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
		verifiCode = String.valueOf(CreateVerifiCodeImage.getVerifiCode());

		// ����֤��ͼƬ�������¼ҳ��
		try {
			ImageIO.write(verifiCodeImage, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// �洢��֤��Session
		request.getSession().setAttribute("verifiCode", verifiCode);
	}

}
