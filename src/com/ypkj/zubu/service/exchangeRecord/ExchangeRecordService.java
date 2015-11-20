package com.ypkj.zubu.service.exchangeRecord;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ypkj.zubu.dao.DaoSupport;
import com.ypkj.zubu.entity.Page;
import com.ypkj.zubu.util.PageData;


@Service("exchangerecordService")
public class ExchangeRecordService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("ExchangeRecordMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("ExchangeRecordMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("ExchangeRecordMapper.edit", pd);
	}
	
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ExchangeRecordMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ExchangeRecordMapper.listAll", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ExchangeRecordMapper.findById", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ExchangeRecordMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

