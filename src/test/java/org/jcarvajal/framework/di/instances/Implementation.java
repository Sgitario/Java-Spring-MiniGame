package org.jcarvajal.framework.di.instances;

import org.jcarvajal.framework.di.annotations.Autowired;
import org.jcarvajal.framework.di.annotations.Init;

public class Implementation implements Interface {
	private String parameter;
	@Autowired
	private Calculated calculated;
	private int timesRunMethodCalled = 0;
	
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	public String getParameter() {
		return parameter;
	}
	
	public void setCalculated(Calculated calculated) {
		this.calculated = calculated;
	}
	
	public Calculated getCalculated() {
		return calculated;
	}
	
	public int getTimesRunMethodCalled() {
		return timesRunMethodCalled;
	}
	
	@Init
	public void runMethod() {
		timesRunMethodCalled++;
	}
}
