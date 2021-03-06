package asteroids.model.programs;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import asteroids.model.Program;
import asteroids.part3.programs.SourceLocation;

public class SequenceStatement extends ProgramElement implements Statement {

	private List<Statement> statements;
	private boolean hasActiveBreakStatement;
	private boolean failedToAdvanceTime;

	public SequenceStatement(List<Statement> statements,
			SourceLocation sourceLocation) {
		super(sourceLocation);
		this.statements = statements;
	}

	@Override
	public void execute() {
		failedToAdvanceTime = false;
		hasActiveBreakStatement = false;
		SourceLocation location = getProgram().getCurrentLocation();
		for(int i = 0; i < statements.size(); i++) {
			Statement statement = statements.get(i);
			SourceLocation nextStatementLocation = (i == statements.size()-1) ? null : statements.get(i+1).getSourceLocation();
			if(i == statements.size()-1 || nextStatementLocation.getLine()> location.getLine()||(nextStatementLocation.getLine()==location.getLine()&&nextStatementLocation.getColumn()>location.getColumn())){
				statement.execute();
				
				if(statement.failedToAdvanceTime()){
					failedToAdvanceTime = true;
					return;
				}
				if(statement.hasActiveBreakStatement()) {
					hasActiveBreakStatement = true;
					return;
				}
			}
		}
	}

	@Override
	public Optional execute(Object[] actualArgs, Set<Variable> localVariables) {
		hasActiveBreakStatement = false;
		for(int i = 0; i < statements.size(); i++) {
			Statement statement = statements.get(i);
			Optional result = statement.execute(actualArgs, localVariables);
			if (result.isPresent()) {
				if (statement.hasActiveBreakStatement()) hasActiveBreakStatement = true;
				return result;
			}
			if (statement.hasActiveBreakStatement()) {
				hasActiveBreakStatement = true;
				return Optional.empty();
			}
		}
		return Optional.empty();
	}

	@Override
	public void setProgram(Program program) {
		super.setProgram(program);
		for(Statement statement : statements) statement.setProgram(program);
	}

	@Override
	public boolean failedToAdvanceTime(){
		return failedToAdvanceTime;
	}

	@Override
	public boolean hasActiveBreakStatement() {
		return hasActiveBreakStatement;
	}

	@Override
	public String toString() {
		return "[SequenceStatement: " + statements.toString() + "]";
	}

}
