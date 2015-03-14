package tets;
@RemotingBean
public class Person1 {
  private String _name;
  private int age;

  public String  getName() {
    return this._name;
  }
  public void setName(String value) {
    this._name = value;
  }

  public int  getAge() {
    return this.age;
  }
  public void setAge(int value) {
    this.age = value;
  }
  static {
    org.bark.remoting.SerializerFactory.getInstance().register(Person1.class, new org.bark.remoting.Serializer(){
      @Override
      public void writeObject(javax.json.JsonObjectBuilder builder, org.bark.remoting.ReferenceProcessor handler, Object value) {
      }
    });
    org.bark.remoting.DeserializerFactory.getInstance().register(Person1.class, new org.bark.remoting.Deserializer(){
      @Override
      public Object readObject(javax.json.JsonObject jsonObj, Object[] handlers, Object obj) {
      }
    });
  }
}