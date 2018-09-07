package cn.tianjin.unifiedfee.ot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tianjin.unifiedfee.ot.entity.Mnsc;
import cn.tianjin.unifiedfee.ot.mapper.MnscMapper;

@Service
public class MnscService {
	@Autowired
	private MnscMapper dao;

	// 获取分页数据
	public List<Mnsc> getPageData() {
		return dao.getPageData();
	}

	// 添加
	public boolean insert(Mnsc entity) throws Exception {
		return dao.insert(entity) > 0 ? true : false;
	}

	// 更新
	public boolean update(Mnsc entity) throws Exception {
		return dao.update(entity) > 0 ? true : false;
	}

	// 删除
	public boolean delete(Mnsc entity) throws Exception {
		return dao.delete(entity) > 0 ? true : false;
	}

	// 保存
	public Mnsc save(Mnsc entity) throws Exception {
		return null;
	}

	// 获取单条信息
	public Mnsc get(Mnsc entity) throws Exception {
		return dao.get(entity.getId());
	}

	// 获取全部信息
	public List<Mnsc> selectAllMsg() {
		return dao.selectAllMsg();
	}
}