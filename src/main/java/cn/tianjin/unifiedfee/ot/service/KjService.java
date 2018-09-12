package cn.tianjin.unifiedfee.ot.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.tianjin.unifiedfee.ot.entity.Kj;
import cn.tianjin.unifiedfee.ot.entity.Mnsc;
import cn.tianjin.unifiedfee.ot.mapper.KjMapper;

@Service
public class KjService {
	@Autowired
	private KjMapper dao;

	// 获取分页数据
	public List<Kj> getPageData(Map<String, Object> param) {
		return dao.getPageData(param);
	}

	// 添加
	public boolean insert(Kj entity) throws Exception {
		return dao.insert(entity) > 0 ? true : false;
	}

	// 更新
	public boolean update(Kj entity) throws Exception {
		return dao.update(entity) > 0 ? true : false;
	}

	// 删除
	public boolean delete(Kj entity) throws Exception {
		return dao.delete(entity) > 0 ? true : false;
	}

	// 保存
	public Mnsc save(Kj entity) throws Exception {
		return null;
	}

	// 获取单条信息
	public Kj get(Kj entity) throws Exception {
		return dao.get(entity.getId());
	}

	// 获取全部信息
	public List<Kj> selectAllMsg() {
		return dao.selectAllMsg();
	}
}