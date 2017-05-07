package asteroids.model.programs;

import java.util.NoSuchElementException;

import asteroids.model.Program;
import asteroids.part3.programs.SourceLocation;

public class ReadVariableExpression extends Expression {

	private String variableName;
	
	public ReadVariableExpression(String variableName,
			SourceLocation sourceLocation) {
		// TODO Auto-generated constructor stub
		super(sourceLocation);
		this.variableName = variableName;
	}
	
	@Override
	public void setProgram(Program program) {
		super.setProgram(program);
	}

	@Override
	public String toString() {
		return "[ReadVariableExpression: " + variableName + "]";
	}
	
	public Function getFunction() {
		return function;
	}

	@Override
	public void setFunction(Function function) {
		this.function = function;
	}

	private Function function;

	@Override
	public Object evaluate() {
		// TODO Auto-generated method stub
		try{
			if (getFunction() != null) return getFunction().getVariable(variableName);
		} catch (NoSuchElementException e){}
		return getProgram().getVariable(variableName);
	}

}
