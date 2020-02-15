namespace java com.mzj.netty.ssy._07_thrift

//---------类型定义（起别名）----------
typedef i16 short
typedef i32 int
typedef i64 long
typedef bool boolean
typedef string String

//---------定义结构体---------
struct Person{
    1: optional String username,//默认是optional，可以不写，但是最好写上
    2: optional int age,
    3: optional boolean married
}

//---------定义异常---------
exception DataException{
    1: optional String message,
    2: optional String callsStack,
    3: optional String date
}

//---------定义service---------
service PersonService{
    Person getPersonByUsername(1:required String username) throws (1: DataException dataException),
    void savePerson(1: required Person person) throws (1: DataException dataException)
}