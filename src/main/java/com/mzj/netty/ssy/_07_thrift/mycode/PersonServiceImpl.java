package com.mzj.netty.ssy._07_thrift.mycode;

import com.mzj.netty.ssy._07_thrift.DataException;
import com.mzj.netty.ssy._07_thrift.Person;
import com.mzj.netty.ssy._07_thrift.PersonService;
import org.apache.thrift.TException;

/**
 * @Auther: mazhongjia
 * @Description:
 */
public class PersonServiceImpl implements PersonService.Iface {
    @Override
    public Person getPersonByUsername(String username) throws DataException, TException {
        System.out.println("getPersonByUsername被调用");
        //处理业务逻辑
        Person person = new Person();
        person.setUsername(username);
        person.setAge(20);
        person.setMarried(false);
        return person;
    }

    @Override
    public void savePerson(Person person) throws DataException, TException {
        System.out.println("savePerson被调用");
        System.out.println(person.getUsername());
        System.out.println(person.getAge());
        System.out.println(person.isMarried());
    }
}
