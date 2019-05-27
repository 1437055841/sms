/**  
 * GitHub address: https://yubuntu0109.github.io/
 * @Title: BasicDao.java   
 * @Package pers.huangyuhui.sms.dao   
 * @Description: �ٿ����ݿ���Ϣ��ͨ�ù���
 * @author: Huang Yuhui     
 * @date: May 10, 2019 2:32:54 PM   
 * @version 1.0
 *
 */
package pers.huangyuhui.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pers.huangyuhui.sms.util.DbUtil;

/**
 * @ClassName: BasicDao
 * @Description: �������ݿ����
 * @author: HuangYuhui
 * @date: May 10, 2019 2:32:54 PM
 * 
 */
public class BasicDao {

	private static Connection connection = DbUtil.getConnection();

	/**
	 * @Title: query
	 * @Description: ��ѯ����
	 * @param: sql
	 * @return: ResultSet
	 */
	public static ResultSet query(String sql) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			return preparedStatement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Title: update
	 * @Description: �ж��Ƿ��Ѹ������ݿ���Ϣ
	 * @param: sql
	 * @return: boolean
	 */
	public boolean update(String sql) {

		try {
			return connection.prepareStatement(sql).executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @Title: releaseConnection
	 * @Description: �ͷ����ݿ�������Դ
	 * @return: void
	 */
//	public static void releaseConnection() {
//		DbUtil.freeResource(null, null, connection);
//	}

}
