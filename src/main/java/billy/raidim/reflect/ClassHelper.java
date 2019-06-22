package billy.raidim.reflect;

@SuppressWarnings("rawtypes")
public class ClassHelper {
	private Class cls=null;
	public ClassHelper(String srg,String mcp) {
		try {
			cls=ClassHelper.class.getClassLoader().loadClass(srg);
		} catch (ClassNotFoundException e) {
			try {
				cls=ClassHelper.class.getClassLoader().loadClass(mcp);
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	public Class get() {
		return cls;
	}
}
