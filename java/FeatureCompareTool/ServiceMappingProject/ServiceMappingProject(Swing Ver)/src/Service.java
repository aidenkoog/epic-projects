public class Service{
	private String SName;
	private String UDName;
	private String Task;
	private String SSName;
	private String InterfaceName;
	private String Component;
	private String Definition;
	private String ServiceID;
	private String RegAuthority;

	public Service(){
		SName = null;
		UDName = null;
		Task = null;
		SSName = null;
		InterfaceName = null;
		Component = null;
		Definition = null;
		ServiceID = null;
		RegAuthority = null;
	}

	public Service(String SName, String UDName, String Task, String SSName, String InterfaceName,
			String Component, String Definition, String ServiceID, String RegAuthority)	{
		setSName(SName);
		setUDName(UDName);
		setTask(Task);
		setSSName(SSName);
		setInterfaceName(InterfaceName);
		setComponent(Component);
		setDefinition(Definition);
		setServiceID(ServiceID);
		setRegAuthority(RegAuthority);
	}

	public void setSName(String sname){
		SName = sname;
	}
	public String getSName(){
		return SName;
	}
	public void setUDName(String udname){
		UDName = udname;
	}
	public String getUDName(){
		return UDName;
	}
	public void setTask(String task){
		Task = task;
	}
	public String getTask(){
		return Task;
	}
	public void setSSName(String ssname){
		SSName = ssname;
	}
	public String getSSName(){
		return SSName;
	}
	public void setInterfaceName(String interfacename){
		InterfaceName = interfacename;
	}
	public String getInterfaceName(){
		return InterfaceName;
	}
	public void setComponent(String component){
		Component = component;
	}
	public String getComponent(){
		return Component;
	}
	public void setDefinition(String definition){
		Definition = definition;
	}
	public String getDefinition(){
		return Definition;
	}
	public void setServiceID(String serviceid){
		ServiceID = serviceid;
	}
	public String getServiceID(){
		return ServiceID;
	}
	public void setRegAuthority(String regauthority){
		RegAuthority = regauthority;
	}
	public String getRegAuthority(){
		return RegAuthority;
	}
}