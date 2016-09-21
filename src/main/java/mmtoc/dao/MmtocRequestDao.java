package mmtoc.dao;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import mmtoc.model.MmtocRequest;

@Transactional
public class MmtocRequestDao implements IMmtocRequestDao {
	
	@Autowired
	private HibernateTemplate  hibernateTemplate;
	public MmtocRequest saveRequest(MmtocRequest request) {
		// TODO Auto-generated method stub
		hibernateTemplate.saveOrUpdate(request);;
		return request;
		
	}
} 