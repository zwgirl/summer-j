package tets;

@RemotingBean
public class Person1 {
  private String _name;
  private byte b;
  private short s;
  private int i;
  private long l;
  private float f;
  private double d;
  private Byte B1;
  private Short S1;
  private Integer I1;
  private Float F1;
  private Double D1;
  private tets.Flag flag;
  private Class<Person1> clazz;
  private Object[] iiil;
  private Object[] iiiq;
  private FirstService fs;
  public String name;
  private Object _parent;

  public String  getName() {
    return this._name;
  }
  public void setName(String value) {
    this._name = value;
  }

  public byte  getB() {
    return this.b;
  }
  public void setB(byte value) {
    this.b = value;
  }

  public short  getS() {
    return this.s;
  }
  public void setS(short value) {
    this.s = value;
  }

  public int  getI() {
    return this.i;
  }
  public void setI(int value) {
    this.i = value;
  }

  public long  getL() {
    return this.l;
  }
  public void setL(long value) {
    this.l = value;
  }

  public float  getF() {
    return this.f;
  }
  public void setF(float value) {
    this.f = value;
  }

  public double  getD() {
    return this.d;
  }
  public void setD(double value) {
    this.d = value;
  }

  public Byte  getB1() {
    return this.B1;
  }
  public void setB1(Byte value) {
    this.B1 = value;
  }

  public Short  getS1() {
    return this.S1;
  }
  public void setS1(Short value) {
    this.S1 = value;
  }

  public Integer  getI1() {
    return this.I1;
  }
  public void setI1(Integer value) {
    this.I1 = value;
  }

  public Float  getF1() {
    return this.F1;
  }
  public void setF1(Float value) {
    this.F1 = value;
  }

  public Double  getD1() {
    return this.D1;
  }
  public void setD1(Double value) {
    this.D1 = value;
  }

  public tets.Flag  getFlag() {
    return this.flag;
  }
  public void setFlag(tets.Flag value) {
    this.flag = value;
  }

  public Class<Person1>  getClazz() {
    return this.clazz;
  }
  public void setClazz(Class<Person1> value) {
    this.clazz = value;
  }

  public Object[]  getIiil() {
    return this.iiil;
  }
  public void setIiil(Object[] value) {
    this.iiil = value;
  }

  public Object[]  getIiiq() {
    return this.iiiq;
  }
  public void setIiiq(Object[] value) {
    this.iiiq = value;
  }

  public FirstService  getFs() {
    return this.fs;
  }
  public void setFs(FirstService value) {
    this.fs = value;
  }

  public Object  getParent() {
    return this._parent;
  }
  public void setParent(Object value) {
    this._parent = value;
  }
  static {
    org.bark.remoting.SerializerFactory.getInstance().register(Person1.class, new org.bark.remoting.Serializer(){
      @Override
      public void writeObject(javax.json.JsonObjectBuilder builder, org.bark.remoting.ReferenceProcessor handler, Object value) {
        Person1 __o = (Person1)value;
        builder = __o._name == null ? builder.addNull("_name") : builder.add("_name", __o._name);
        builder.add("b", __o.b);
        builder.add("s", __o.s);
        builder.add("i", __o.i);
        builder.add("l", __o.l);
        builder.add("f", __o.f);
        builder.add("d", __o.d);
        builder = __o.B1 == null ? builder.addNull("B1") : builder.add("B1", (byte)__o.B1);
        builder = __o.S1 == null ? builder.addNull("S1") : builder.add("S1", (short)__o.S1);
        builder = __o.I1 == null ? builder.addNull("I1") : builder.add("I1", (int)__o.I1);
        builder = __o.F1 == null ? builder.addNull("F1") : builder.add("F1", (float)__o.F1);
        builder = __o.D1 == null ? builder.addNull("D1") : builder.add("D1", (double)__o.D1);
        builder = __o.flag == null ? builder.addNull("flag") : builder.add("flag", __o.flag.name());
        builder = __o.clazz == null ? builder.addNull("clazz") : builder.add("clazz", __o.clazz.getName());
        builder = __o.iiil == null ? builder.addNull("iiil") : builder.add("iiil", handler.shared(__o.iiil));
        builder = __o.iiiq == null ? builder.addNull("iiiq") : builder.add("iiiq", handler.shared(__o.iiiq));
        builder = __o.fs == null ? builder.addNull("fs") : builder.add("fs", handler.shared(__o.fs));
        builder = __o._parent == null ? builder.addNull("_parent") : builder.add("_parent", handler.shared(__o._parent));
      }
    });
    org.bark.remoting.DeserializerFactory.getInstance().register(Person1.class, new org.bark.remoting.Deserializer(){
      @SuppressWarnings({ "unchecked", "rawtypes" })
      @Override
      public Object readObject(javax.json.JsonObject jsonObj, Object[] handlers, Object obj) throws Exception {
        Person1 __o = (Person1)obj;
        __o._name = jsonObj.get("_name") == javax.json.JsonValue.NULL ? null : jsonObj.getString("_name");
        __o.b = (byte)jsonObj.getInt("b");
        __o.s = (short)jsonObj.getInt("s");
        __o.i = jsonObj.getInt("i");
        __o.l = (long)jsonObj.getInt("l");
        __o.f = (float)jsonObj.getJsonNumber("f").doubleValue();
        __o.d = (double)jsonObj.getJsonNumber("d").doubleValue();
        __o.B1 = jsonObj.get("B1") == javax.json.JsonValue.NULL ? null : (byte)jsonObj.getJsonNumber("B1").intValue();
        __o.S1 = jsonObj.get("S1") == javax.json.JsonValue.NULL ? null : (short)jsonObj.getJsonNumber("S1").intValue();
        __o.I1 = jsonObj.get("I1") == javax.json.JsonValue.NULL ? null : jsonObj.getJsonNumber("I1").intValue();
        __o.F1 = jsonObj.get("F1") == javax.json.JsonValue.NULL ? null : (float)jsonObj.getJsonNumber("F1").doubleValue();
        __o.D1 = jsonObj.get("D1") == javax.json.JsonValue.NULL ? null : (double)jsonObj.getJsonNumber("D1").doubleValue();
        __o.flag = jsonObj.get("flag") == javax.json.JsonValue.NULL ? null : tets.Flag.valueOf(jsonObj.getString("flag"));
        __o.clazz = jsonObj.get("clazz") == javax.json.JsonValue.NULL ? null : (Class)Class.forName(jsonObj.getString("clazz"));
        __o.iiil = jsonObj.get("iiil") == javax.json.JsonValue.NULL ? null : (Object[])handlers[jsonObj.getInt("iiil")];
        __o.iiiq = jsonObj.get("iiiq") == javax.json.JsonValue.NULL ? null : (Object[])handlers[jsonObj.getInt("iiiq")];
        __o.fs = jsonObj.get("fs") == javax.json.JsonValue.NULL ? null : (  FirstService)handlers[jsonObj.getInt("fs")];
        __o._parent = jsonObj.get("_parent") == javax.json.JsonValue.NULL ? null : handlers[jsonObj.getInt("_parent")];
        return obj;
      }
    });
  }
}
@RemotingBean
enum Flag {


}
@RemotingBean
interface TT {
}