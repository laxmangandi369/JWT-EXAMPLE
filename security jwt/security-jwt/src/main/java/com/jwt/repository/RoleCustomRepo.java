package com.jwt.repository;

import com.jwt.model.Role;
import com.jwt.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Role> getRole(User user){
        StringBuilder sql = new StringBuilder()
                .append("SELECT r.name as name\n" +
                        "FROM users u\n" +
                        "JOIN user_role ur ON u.user_id=ur.user_id\n" +
                        "JOIN roles r ON r.id=ur.role_id\n" +
                        "Where 1=1");
        if (user.getEmail()!=null){
            sql.append(" and email=:email");
        }

        NativeQuery<Role> query = ((Session) entityManager.getDelegate()).createNativeQuery(sql.toString());
        if (user.getEmail()!=null){
            query.setParameter("email",user.getEmail());

        }
        query.addScalar("name", StandardBasicTypes.STRING);
        query.setResultListTransformer(Transformers.aliasToBean(Role.class));
        return query.list();
    }
}
