/**  
 * GitHub address: https://yubuntu0109.github.io/
 * @Title: TeacherDao.java   
 * @Package pers.huangyuhui.sms.dao   
 * @Description: �������ݿ�
 * @author: Huang Yuhui     
 * @date: May 20, 2019 8:05:53 AM   
 * @version 1.0
 *
 */
package pers.huangyuhui.sms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pers.huangyuhui.sms.model.Paging;
import pers.huangyuhui.sms.model.TeacherInfo;
import pers.huangyuhui.sms.util.DbUtil;
import pers.huangyuhui.sms.util.StringUtil;

/**
 * @ClassName: TeacherDao
 * @Description: ������ʦ��Ϣ
 * @author: HuangYuhui
 * @date: May 20, 2019 8:05:53 AM
 * 
 */
public class TeacherDao extends BasicDao {

	/**
	 * @Title: getUserInfo
	 * @Description: ��ȡ�û���Ϣ
	 * @param: name
	 * @param: password
	 * @return: TeacherInfo
	 */
	public TeacherInfo getUserInfo(String name, String password) {
		String sql = "select id,classID,tno,name,password,sex,email,mobile,photo from user_teacher where name='" + name
				+ "'  and password='" + password + "'";

		try (ResultSet resultSet = query(sql)) {
			if (resultSet.next()) {
				TeacherInfo teacherInfo = new TeacherInfo();
				teacherInfo.setId(resultSet.getInt("id"));
				teacherInfo.setClassID(resultSet.getInt("classID"));
				teacherInfo.setTno(resultSet.getString("tno"));
				teacherInfo.setName(resultSet.getString("name"));
				teacherInfo.setPassword(resultSet.getString("password"));
				teacherInfo.setSex(resultSet.getString("sex"));
				teacherInfo.setEmail(resultSet.getString("email"));
				teacherInfo.setMobile(resultSet.getString("mobile"));
				teacherInfo.setPhoto(resultSet.getBinaryStream("photo"));

				return teacherInfo;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Title: addTeacher
	 * @Description: ��ӽ�ʦ
	 * @param: teacherInfo
	 * @return: boolean
	 */
	public boolean addTeacher(TeacherInfo teacherInfo) {

		String sql = null;
		if (!StringUtil.isEmpty(teacherInfo.toString())) {
			sql = "insert into user_teacher(classID,tno,name,password,sex,email,mobile) values('"
					+ teacherInfo.getClassID() + "','" + teacherInfo.getTno() + "','" + teacherInfo.getName() + "' , '"
					+ teacherInfo.getPassword() + "' , '" + teacherInfo.getSex() + "' , '" + teacherInfo.getEmail()
					+ "' , '" + teacherInfo.getMobile() + "')";
		}

		return update(sql);
	}

	/**
	 * @Title: getTeacherList
	 * @Description: ��ȡ��ʦ�б�
	 * @param: teacherInfo
	 * @param: paging
	 * @return: List<TeacherInfo>
	 */
	public List<TeacherInfo> getTeacherList(TeacherInfo teacherInfo, Paging paging) {

		List<TeacherInfo> list = new ArrayList<TeacherInfo>();
		String sql = "select * from user_teacher ";

		if (!StringUtil.isEmpty(teacherInfo.getName())) {
			sql += " and name like '%" + teacherInfo.getName() + "%' ";
		}
		if (teacherInfo.getClassID() != 0) {
			sql += " and classID = " + teacherInfo.getClassID();
		}
		if (teacherInfo.getId() != 0) {
			sql += " and id = " + teacherInfo.getId();// Լ���û�Ȩ��
		}
		sql += " limit " + paging.getPageStart() + "," + paging.getPageSize();// ��ҳ��ѯ

		try (ResultSet resultSet = query(sql.replaceFirst("and", "where"))) {
			while (resultSet.next()) {
				TeacherInfo teacherInfo2 = new TeacherInfo();
				teacherInfo2.setClassID(resultSet.getInt("classID"));
				teacherInfo2.setId(resultSet.getInt("id"));
				teacherInfo2.setTno(resultSet.getString("tno"));
				teacherInfo2.setName(resultSet.getString("name"));
				teacherInfo2.setSex(resultSet.getString("sex"));
				teacherInfo2.setEmail(resultSet.getString("email"));
				teacherInfo2.setMobile(resultSet.getString("mobile"));
				list.add(teacherInfo2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	/**
	 * @Title: getTeacherListNum
	 * @Description: ��ȡ��ʦ�б������
	 * @param: teacherInfo
	 * @return: int
	 */
	public int getTeacherListNum(TeacherInfo teacherInfo) {

		int num = 0;
		String sql = "select count(*) as num from user_teacher ";

		if (!StringUtil.isEmpty(teacherInfo.getName())) {
			sql += "and name like '%" + teacherInfo.getName() + "%' ";
		}
		if (teacherInfo.getClassID() != 0) {
			sql += "and classID = " + teacherInfo.getClassID();
		}
		if (teacherInfo.getId() != 0) {
			sql += " and id = " + teacherInfo.getId();// Լ���û�Ȩ��
		}

		try (ResultSet resultSet = query(sql.replaceFirst("and", "where"))) {
			while (resultSet.next()) {
				num = resultSet.getInt("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return num;
	}

	/**
	 * @Title: editTeacherInfo
	 * @Description: ���½�ʦ��Ϣ
	 * @param: teacherInfo
	 * @return: boolean
	 */
	public boolean editTeacherInfo(TeacherInfo teacherInfo) {

		// Ϊ��ֹ�쳣:Data truncation: Truncated incorrect DOUBLE value,ʹ�� ' , ' ���� ' and ' ~
		String sql = "update user_teacher set name='" + teacherInfo.getName() + "' ,  sex='" + teacherInfo.getSex()
				+ "' ,  email='" + teacherInfo.getEmail() + "' ,  mobile='" + teacherInfo.getMobile() + "' ,  classID='"
				+ teacherInfo.getClassID() + "' where id= " + teacherInfo.getId();
		return update(sql);
	}

	/**
	 * @Title: deleteTeacher
	 * @Description: ɾ����ʦ
	 * @param: ids
	 * @return: boolean
	 */
	public boolean deleteTeacher(String ids) {

		String sql = "delete from user_teacher where id in ( " + ids + " )";
		return update(sql);
	}

	/**
	 * @Title: getTeacherInfoById
	 * @Description: ��ȡ��ʦ��Ϣ
	 * @param: id
	 * @return: TeacherInfo
	 */
	public TeacherInfo getTeacherInfoById(int id) {

		String sql = "select * from user_teacher where id =" + id;
		TeacherInfo teacherInfo = null;

		try (ResultSet resultSet = query(sql)) {
			if (resultSet.next()) {
				teacherInfo = new TeacherInfo();
				teacherInfo.setId(resultSet.getInt("id"));
				teacherInfo.setClassID(resultSet.getInt("classID"));
				teacherInfo.setTno(resultSet.getString("tno"));
				teacherInfo.setName(resultSet.getString("name"));
				teacherInfo.setPassword(resultSet.getString("password"));
				teacherInfo.setSex(resultSet.getString("sex"));
				teacherInfo.setEmail(resultSet.getString("email"));
				teacherInfo.setMobile(resultSet.getString("mobile"));
				teacherInfo.setPhoto(resultSet.getBinaryStream("photo"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return teacherInfo;
	}

	/**
	 * @Title: setStudentPhoto
	 * @Description: �����û�ͷ��
	 * @param: teacherInfo
	 * @return: boolean
	 */
	public boolean setTeacherPhoto(TeacherInfo teacherInfo) {

		Connection connection = DbUtil.getConnection();
		String sql = "update user_teacher set photo = ? where id = ?";

		try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
			prepareStatement.setBinaryStream(1, teacherInfo.getPhoto());
			prepareStatement.setInt(2, teacherInfo.getId());
			return prepareStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return update(sql);
	}

	/**
	 * @Title: modifyPassword
	 * @Description: �޸�����
	 * @param: adminInfo
	 * @param: newPassword
	 * @return: boolean
	 */
	public boolean modifyPassword(TeacherInfo teacherInfo, String newPassword) {
		String sql = "update user_teacher set password = '" + newPassword + "' where id = '" + teacherInfo.getId()
				+ "'";
		return update(sql);
	}

}
