package com.fcl.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.fcl.compiler.antlr4.FCLParser.AttrContext;
import com.fcl.compiler.antlr4.FCLParser.AttrStatContext;
import com.fcl.compiler.antlr4.FCLParser.BlockContext;
import com.fcl.compiler.antlr4.FCLParser.BooleandtContext;
import com.fcl.compiler.antlr4.FCLParser.BooleandtExpContext;
import com.fcl.compiler.antlr4.FCLParser.BreakStatContext;
import com.fcl.compiler.antlr4.FCLParser.ChunkContext;
import com.fcl.compiler.antlr4.FCLParser.DiscardStatContext;
import com.fcl.compiler.antlr4.FCLParser.ExpContext;
import com.fcl.compiler.antlr4.FCLParser.ExplistContext;
import com.fcl.compiler.antlr4.FCLParser.ForStatContext;
import com.fcl.compiler.antlr4.FCLParser.ForeachStatContext;
import com.fcl.compiler.antlr4.FCLParser.IdentifierExpContext;
import com.fcl.compiler.antlr4.FCLParser.IfStatContext;
import com.fcl.compiler.antlr4.FCLParser.ListdtContext;
import com.fcl.compiler.antlr4.FCLParser.ListdtExpContext;
import com.fcl.compiler.antlr4.FCLParser.MoveStatContext;
import com.fcl.compiler.antlr4.FCLParser.NilExpContext;
import com.fcl.compiler.antlr4.FCLParser.NumberdtContext;
import com.fcl.compiler.antlr4.FCLParser.NumberdtExpContext;
import com.fcl.compiler.antlr4.FCLParser.ObjectdtContext;
import com.fcl.compiler.antlr4.FCLParser.ObjectdtExpContext;
import com.fcl.compiler.antlr4.FCLParser.ObjlistContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorAddSubContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorAddSubExpContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorAndContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorAndExpContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorComparisonContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorComparisonExpContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorMulDivModContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorMulDivModExpContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorOrContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorOrExpContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorPowerContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorPowerExpContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorStrcatContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorStrcatExpContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorUnaryContext;
import com.fcl.compiler.antlr4.FCLParser.OperatorUnaryExpContext;
import com.fcl.compiler.antlr4.FCLParser.PickupStatContext;
import com.fcl.compiler.antlr4.FCLParser.PositiondtArrayIdentifierIdentifierContext;
import com.fcl.compiler.antlr4.FCLParser.PositiondtArrayIdentifierNumberContext;
import com.fcl.compiler.antlr4.FCLParser.PositiondtContext;
import com.fcl.compiler.antlr4.FCLParser.PositiondtExpContext;
import com.fcl.compiler.antlr4.FCLParser.PositiondtIdentifierContext;
import com.fcl.compiler.antlr4.FCLParser.PositiondtNumberContext;
import com.fcl.compiler.antlr4.FCLParser.PositiondtRangeIdentifierContext;
import com.fcl.compiler.antlr4.FCLParser.PositiondtRangeNumberContext;
import com.fcl.compiler.antlr4.FCLParser.PrintexpContext;
import com.fcl.compiler.antlr4.FCLParser.PrintexpStatContext;
import com.fcl.compiler.antlr4.FCLParser.ProcedureContext;
import com.fcl.compiler.antlr4.FCLParser.ProcedureStatContext;
import com.fcl.compiler.antlr4.FCLParser.ProcedurecallContext;
import com.fcl.compiler.antlr4.FCLParser.ProcedurecallExpContext;
import com.fcl.compiler.antlr4.FCLParser.ProcedurecallStatContext;
import com.fcl.compiler.antlr4.FCLParser.RetstatContext;
import com.fcl.compiler.antlr4.FCLParser.SemicolonStatContext;
import com.fcl.compiler.antlr4.FCLParser.StatContext;
import com.fcl.compiler.antlr4.FCLParser.StringdtContext;
import com.fcl.compiler.antlr4.FCLParser.StringdtExpContext;
import com.fcl.compiler.antlr4.FCLParser.VarlistContext;
import com.fcl.compiler.antlr4.FCLParser.WhileStatContext;
import com.fcl.compiler.antlr4.FCLVisitor;

class FCLVisitorImpl implements FCLVisitor<Integer> {
	private static final Logger Log = Logger.getLogger( FCLVisitorImpl.class.getName() );
	
	public enum ReturnCode {
	    BLOCK_RETURNED(1, "The block finished with a return statement. For internal use only."),
	    UNDEFINED_ERROR(-1, "Undefined."),
	    FOREACH_UNIMPLEMENTED(-2, "The foreach block is yet to be implemented."),
	    MUST_BE_RANGE(-10, "The identifier on the \"for\" statement must be a range.");
	    
	    private static Map<Integer, ReturnCode> map = new HashMap<Integer, ReturnCode>();

	    static {
		for (ReturnCode retEnum : ReturnCode.values()) {
		    map.put(retEnum.code, retEnum);
		}
	    }
	    
	    public static ReturnCode valueOf(int code) {
		return map.get(code);
	    }
	    
	    private Integer code;
            private String message;
            
            private ReturnCode(Integer code, String message) {
            	this.code = code;
            	this.message = message;
            }
            
            public Integer getCode() {
            	return code;
            }
            
            public String getMessage() {
            	return message;
            }
	}
	
	// List of instructions
	private List<String> instructions = new ArrayList<>();
	
	// Used by varlist and objlist visitors
	private List<String> varlist = new ArrayList<>();
	private List<String> objlist = new ArrayList<>();
	
	// Used for naming labels
	private Integer conditionalLabelCounter = 0;
	
	// Used for keeping track of break labels within nested loops
	private Stack<String> breakLabels = new Stack<>();
	
	@Override
	public Integer visit(ParseTree arg0) {
		ParseTree child = arg0.getChild(0);
		
		if(child instanceof ChunkContext) {
			return visitChunk((ChunkContext) child);
		} else if(child instanceof BlockContext) {
			return visitBlock((BlockContext) child);
		}
		
		return -1;
	}
	
	// CHUNK ====================================
	
	@Override
	public Integer visitChunk(ChunkContext ctx) {
		Log.info("Entering Chunk.");
		return visitBlock(ctx.block());
	}
	
	// BLOCK ====================================
	
	@Override
	public Integer visitBlock(BlockContext ctx) {
		Log.info("Entering Block");
		
		// Visit every statement.
		Integer retVal = 0;
		for(StatContext statCtx : ctx.stat()) {
			
			if(statCtx instanceof SemicolonStatContext) {
				retVal = visitSemicolonStat((SemicolonStatContext) statCtx);
				
			} else if(statCtx instanceof BreakStatContext) {
				retVal = visitBreakStat((BreakStatContext) statCtx);
				
			} else if(statCtx instanceof WhileStatContext) {
				retVal = visitWhileStat((WhileStatContext) statCtx);
				
			} else if(statCtx instanceof ForStatContext) {
				retVal = visitForStat((ForStatContext) statCtx);
				
			} else if(statCtx instanceof ForeachStatContext) {
				retVal = visitForeachStat((ForeachStatContext) statCtx);
				
			} else if(statCtx instanceof IfStatContext) {
				retVal = visitIfStat((IfStatContext) statCtx);
				
			} else if(statCtx instanceof ProcedurecallStatContext) {
				retVal = visitProcedurecallStat((ProcedurecallStatContext) statCtx);
				
			} else if(statCtx instanceof ProcedureStatContext) {
				retVal = visitProcedureStat((ProcedureStatContext) statCtx);
				
			} else if(statCtx instanceof AttrStatContext) {
				retVal = visitAttrStat((AttrStatContext) statCtx);
			} else if(statCtx instanceof PrintexpStatContext) {
				retVal = visitPrintexpStat((PrintexpStatContext) statCtx);
			} else if(statCtx instanceof MoveStatContext) {
				retVal = visitMoveStat((MoveStatContext) statCtx);
			} else if(statCtx instanceof PickupStatContext) {
				retVal = visitPickupStat((PickupStatContext) statCtx);
			} else if(statCtx instanceof DiscardStatContext) {
				retVal = visitDiscardStat((DiscardStatContext) statCtx);
			}
			
			if(retVal < 0) 
				break;
		}
		
		// Visit return statement
		RetstatContext retstat = ctx.retstat();
		if(retstat != null && retVal == 0) {
			retVal = visitRetstat(retstat);
			return ReturnCode.BLOCK_RETURNED.getCode();
		}
		
		return retVal;
	}
	
	// STATEMENTS ===============================
	
	@Override
	public Integer visitSemicolonStat(SemicolonStatContext ctx) {
		Log.info("Entering Semicolon Statement.");
		
		return 0;
	}
	
	@Override
	public Integer visitBreakStat(BreakStatContext ctx) {
		Log.info("Entering Break Statement.");
		
		if(breakLabels.isEmpty()) {
			Log.warning("Break statement found outside a loop.");
		} else {
			instructions.add("JMP " + breakLabels.peek());
		}
		
		return 0;
	}
	
	@Override
	public Integer visitWhileStat(WhileStatContext ctx) {
		Log.info("Entering While Statement.");
		
		String startLabel = nextLabel();
		String endLabel = nextLabel();
		
		breakLabels.push(endLabel);
		
		instructions.add("LABEL " + startLabel);
		visitExp(ctx.exp());
		instructions.add("JMPFALSE " + endLabel);
		
		visitBlock(ctx.block());
		
		instructions.add("JMP " + startLabel);
		instructions.add("LABEL " + endLabel);
		
		breakLabels.pop();
		
		return 0;
	}
	
	@Override
	public Integer visitForStat(ForStatContext ctx) {
		Log.info("Entering For Statement.");
		
		// Find out the step
		Integer step = 1;
		if(ctx.numberdt() != null) {
			step = Integer.parseInt(ctx.numberdt().getText());
		}
		
		// Find out the range
		String startPos;
		String endPos;
		if(ctx.positiondt() instanceof PositiondtRangeNumberContext) {
			startPos = ((PositiondtRangeNumberContext) ctx.positiondt()).INT(0).getText();
			endPos = ((PositiondtRangeNumberContext) ctx.positiondt()).INT(1).getText();
		} else if(ctx.positiondt() instanceof PositiondtRangeIdentifierContext) {
			startPos = ((PositiondtRangeIdentifierContext) ctx.positiondt()).Identifier(0).getText();
			endPos = ((PositiondtRangeIdentifierContext) ctx.positiondt()).Identifier(1).getText();
		} else {
			return ReturnCode.MUST_BE_RANGE.getCode();
		}
		
		//Save the initial position to the variable
		instructions.add("PUSH [" + startPos + "]");
		instructions.add("STORE " + ctx.Identifier().getText());
		
		String startLabel = nextLabel();
		
		instructions.add("LABEL " + startLabel);
		
		visitBlock(ctx.block());
		
		// First, check if the end position has been reached
		instructions.add("LOAD " + ctx.Identifier());
		instructions.add("PUSH [" + endPos + "]");
		instructions.add("COMPARE ==");
		
		// Increment the variable, and store it (it will leave the stack)
		instructions.add("LOAD " + ctx.Identifier());
		instructions.add("PUSH " + step);
		instructions.add("ADD");
		instructions.add("STORE " + ctx.Identifier());
		
		// Jump back (repeat) if false (using the result of the last comparison, which will be on the top of the stack)
		instructions.add("JMPFALSE " + startLabel);
		
		return 0;
	}
	
	@Override
	public Integer visitForeachStat(ForeachStatContext ctx) {
		Log.info("Entering Foreach Statement.");
		
		// TODO : Iterate
		
		return ReturnCode.FOREACH_UNIMPLEMENTED.code;
	}
	
	@Override
	public Integer visitProcedurecallStat(ProcedurecallStatContext ctx) {
		Log.info("Entering Procedurecall Statement.");
		
		return visitProcedurecall(ctx.procedurecall());
	}

	@Override
	public Integer visitProcedureStat(ProcedureStatContext ctx) {
		Log.info("Entering Procedure Statement.");
		
		return visitProcedure(ctx.procedure());
	}
	
	@Override
	public Integer visitIfStat(IfStatContext ctx) {
		Log.info("Entering If Statement");
		
		String endLabel = nextLabel();
		for(int i = 0; i < ctx.exp().size(); i++) {
			String blockEndlabel = nextLabel();
			visitExp(ctx.exp(i));
			
			instructions.add("JMPFALSE " + blockEndlabel);
			
			// After finishing a block, jump to the end of the if/else if/else statements.
			visitBlock(ctx.block(i));
			instructions.add("JMP " + endLabel);
			
			instructions.add("LABEL " + blockEndlabel);
		}
		
		// This means we have an else statement (i.e. else statements don't have expressions)
		if(ctx.block().size() > ctx.exp().size()) {
			visitBlock(ctx.block(ctx.block().size()-1));
		}
		
		instructions.add("LABEL " + endLabel);
		
		return 0;
	}
	
	@Override
	public Integer visitAttrStat(AttrStatContext ctx) {
		Log.info("Entering Attr Statement");
		
		return visitAttr(ctx.attr());
	}
	
	@Override
	public Integer visitPrintexpStat(PrintexpStatContext ctx) {
		Log.info("Entering Printexp Statement");
		
		return visitPrintexp(ctx.printexp());
	}
	
	@Override
	public Integer visitMoveStat(MoveStatContext ctx) {
		visitExp(ctx.exp(0));
		visitExp(ctx.exp(1));
		
		instructions.add("MOVE");
		
		return 0;
	}

	@Override
	public Integer visitPickupStat(PickupStatContext ctx) {
		visitExp(ctx.exp(0));
		visitExp(ctx.exp(1));
		
		instructions.add("PICKUP");
		
		return 0;
	}

	@Override
	public Integer visitDiscardStat(DiscardStatContext ctx) {
		instructions.add("DISCARD");
		return 0;
	}
	
	@Override
	public Integer visitProcedure(ProcedureContext ctx) {
		Log.info("Entering Procedure \"" + ctx.Identifier().getText() + "\".");
		
		visitVarlist(ctx.varlist());
		visitObjlist(ctx.objlist());
		
		// Procedure label: PROCEDURE name $var1 $var2 ... &obj1:Sample &obj2:Plate ... :
		String label = "PROCEDURE " + ctx.Identifier().getText();
		
		// Add variables
		for(String var : varlist) {
			label += " $" + var;
		}
		
		// Add objects
		for(String obj : objlist) {
			label += " &" + obj;
		}
		
		instructions.add(label);
		
		Integer retVal = visitBlock(ctx.block());
		
		if(retVal < 0) {
			return retVal;
		}
		
		// If the block did not finish with a return statement
		if(retVal != ReturnCode.BLOCK_RETURNED.getCode()) {
			instructions.add("RETURN");
		}
		
		return 0;
	}
	
	@Override
	public Integer visitProcedurecall(ProcedurecallContext ctx) {
		Log.info("Entering Procedurecall");
		
		//Add parameters
		visitExplist(ctx.explist());
		visitVarlist(ctx.varlist());
		
		for(String var : varlist) {
			instructions.add("PUSH " + var);
		}
		
		instructions.add("CALL " + ctx.Identifier().getText());
		
		return 0;
	}
	
	@Override
	public Integer visitObjlist(ObjlistContext ctx) {
		Log.info("Entering Objlist");
		
		// Add objects name:type (ex: &obj1:Sample)
		objlist.clear();
		if(ctx != null) {
			for(int i = 0; i < ctx.Identifier().size(); i++) {
				visitObjectdt(ctx.objectdt(i));
				objlist.add(ctx.Identifier(i).getText() + ":" + ctx.objectdt(i).getText());
			}
		}
		
		return 0;
	}
	
	@Override
	public Integer visitAttr(AttrContext ctx) {
		Log.info("Entering Attr");
		
		// Resolve and store the expression
		visitExp(ctx.exp());
		instructions.add("STORE " + ctx.Identifier().getText());
		
		return 0;
	}
	
	@Override
	public Integer visitVarlist(VarlistContext ctx) {
		Log.info("Entering Varlist");
		
		varlist.clear();
		if(ctx != null) {
			if(ctx.Identifier().size() > 0) {
				for(TerminalNode identifier : ctx.Identifier()) {
					varlist.add(identifier.getText());
				}
			} else if(ctx.objectdt().size() > 0) {
				for(ObjectdtContext objectdtCtx : ctx.objectdt()) {
					varlist.add(objectdtCtx.getText());
				}
			}
		}
		
		return 0;
	}
	
	@Override
	public Integer visitRetstat(RetstatContext ctx) {
		Log.info("Entering Retstat");
		
		visitExp(ctx.exp());
		
		instructions.add("RETURN");
		
		return 0;
	}
	
	@Override
	public Integer visitExplist(ExplistContext ctx) {
		Log.info("Entering Explist");
		
		if (ctx != null) {
			for(ExpContext expCtx : ctx.exp()) {
				visitExp(expCtx);
			}
		}
		
		return 0;
	}
	
	@Override
	public Integer visitPrintexp(PrintexpContext ctx) {
		Log.info("Entering Printexp");
		
		visitExp(ctx.exp());
		instructions.add("PRINT");
		instructions.add("POP");
		
		return 0;
	}
	
	public void dumpInstructions() {
		for(String instruction : instructions) {
			System.out.println(instruction);
		}
	}
	
	public List<String> getInstructions() {
		return instructions;
	}
	
	// EXPRESSIONS ==============================
	
	public Integer visitExp(ExpContext ctx) {
		if(ctx == null) {
			return 0;
		}
		
		Integer retVal = 0;
		
		if(ctx instanceof NilExpContext) {
			retVal = visitNilExp((NilExpContext) ctx);
			
		} else if(ctx instanceof NumberdtExpContext) {
			retVal = visitNumberdtExp((NumberdtExpContext) ctx);
			
		} else if(ctx instanceof BooleandtExpContext) {
			retVal = visitBooleandtExp((BooleandtExpContext) ctx);
			
		} else if(ctx instanceof StringdtExpContext) {
			retVal = visitStringdtExp((StringdtExpContext) ctx);
			
		} else if(ctx instanceof PositiondtExpContext) {
			retVal = visitPositiondtExp((PositiondtExpContext) ctx);
			
		} else if(ctx instanceof ObjectdtExpContext) {
			retVal = visitObjectdtExp((ObjectdtExpContext) ctx);
			
		} else if(ctx instanceof ListdtExpContext) {
			retVal = visitListdtExp((ListdtExpContext) ctx);
			
		} else if(ctx instanceof ProcedurecallExpContext) {
			retVal = visitProcedurecallExp((ProcedurecallExpContext) ctx);
			
		} else if(ctx instanceof IdentifierExpContext) {
			retVal = visitIdentifierExp((IdentifierExpContext) ctx);
			
		} else if(ctx instanceof OperatorPowerExpContext) {
			retVal = visitOperatorPowerExp((OperatorPowerExpContext) ctx);
			
		} else if(ctx instanceof OperatorUnaryExpContext) {
			retVal = visitOperatorUnaryExp((OperatorUnaryExpContext) ctx);
			
		} else if(ctx instanceof OperatorMulDivModExpContext) {
			retVal = visitOperatorMulDivModExp((OperatorMulDivModExpContext) ctx);
			
		} else if(ctx instanceof OperatorAddSubExpContext) {
			retVal = visitOperatorAddSubExp((OperatorAddSubExpContext) ctx);
			
		} else if(ctx instanceof OperatorStrcatExpContext) {
			retVal = visitOperatorStrcatExp((OperatorStrcatExpContext) ctx);
			
		} else if(ctx instanceof OperatorComparisonExpContext) {
			retVal = visitOperatorComparisonExp((OperatorComparisonExpContext) ctx);
			
		} else if(ctx instanceof OperatorAndExpContext) {
			retVal = visitOperatorAndExp((OperatorAndExpContext) ctx);
			
		} else if(ctx instanceof OperatorOrExpContext) {
			retVal = visitOperatorOrExp((OperatorOrExpContext) ctx);
		}
		
		return retVal;
	}
	
	@Override
	public Integer visitNilExp(NilExpContext ctx) {
		Log.info("Entering Nil Expression");
		
		instructions.add("PUSH nil");
		
		return 0;
	}

	@Override
	public Integer visitNumberdtExp(NumberdtExpContext ctx) {
		Log.info("Entering Numberdt Expression");
		
		instructions.add("PUSH " + ctx.getText());
		
		return 0;
	}

	@Override
	public Integer visitBooleandtExp(BooleandtExpContext ctx) {
		Log.info("Entering Booleandt Expression");
		
		instructions.add("PUSH " + ctx.getText());
		
		return 0;
	}
	
	@Override
	public Integer visitStringdtExp(StringdtExpContext ctx) {
		Log.info("Entering Stringdt Expression");
		
		instructions.add("PUSH " + ctx.getText());
		
		return 0;
	}
	
	@Override
	public Integer visitPositiondtExp(PositiondtExpContext ctx) {
		Log.info("Entering Positiondt Expression");
		
		instructions.add("PUSH " + ctx.getText());
		
		return 0;
	}
	
	@Override
	public Integer visitObjectdtExp(ObjectdtExpContext ctx) {
		Log.info("Entering Objectdt Expression");
		
		instructions.add("PUSH " + ctx.getText());
		
		return 0;
	}
	
	@Override
	public Integer visitListdtExp(ListdtExpContext ctx) {
		Log.info("Entering Listdt Expression");
		
		instructions.add("PUSH " + ctx.getText());
		
		return 0;
	}
	
	@Override
	public Integer visitProcedurecallExp(ProcedurecallExpContext ctx) {
		Log.info("Entering Procedurecall Expression");
		
		visitProcedurecall(ctx.procedurecall());
		
		return 0;
	}
	
	@Override
	public Integer visitIdentifierExp(IdentifierExpContext ctx) {
		Log.info("Entering Identifier Expression");
		
		instructions.add("LOAD " + ctx.Identifier().getText());
		
		return 0;
	}
	
	@Override
	public Integer visitOperatorPowerExp(OperatorPowerExpContext ctx) {
		Log.info("Entering OperatorPower Expression");
		
		visitExp(ctx.exp(0));
		visitExp(ctx.exp(1));
		instructions.add("POW");
		
		return 0;
	}
	
	@Override
	public Integer visitOperatorUnaryExp(OperatorUnaryExpContext ctx) {
		Log.info("Entering OperatorUnary Expression");
		
		visitExp(ctx.exp());
		
		switch(ctx.operatorUnary().getText()) {
		case "!":
			instructions.add("NOT");
			break;
		}
		
		return 0;
	}
	
	@Override
	public Integer visitOperatorMulDivModExp(OperatorMulDivModExpContext ctx) {
		Log.info("Entering OperatorMulDivMod Expression");
		
		visitExp(ctx.exp(0));
		visitExp(ctx.exp(1));
		
		switch(ctx.operatorMulDivMod().getText()) {
		case "*":
			instructions.add("MUL");
			break;
		case "/":
			instructions.add("DIV");
			break;
		case "%":
			instructions.add("MOD");
			break;
		}
		
		return 0;
	}
	
	@Override
	public Integer visitOperatorAddSubExp(OperatorAddSubExpContext ctx) {
		Log.info("Entering OperatorAddSub Expression");
		
		visitExp(ctx.exp(0));
		visitExp(ctx.exp(1));
		
		switch(ctx.operatorAddSub().getText()) {
		case "+":
			instructions.add("ADD");
			break;
		case "-":
			instructions.add("SUB");
		}
		
		return 0;
	}
	
	@Override
	public Integer visitOperatorStrcatExp(OperatorStrcatExpContext ctx) {
		Log.info("Entering OperatorStrcat Expression");
		
		visitExp(ctx.exp(0));
		visitExp(ctx.exp(1));
		
		instructions.add("CONCAT");
		
		return 0;
	}
	
	@Override
	public Integer visitOperatorComparisonExp(OperatorComparisonExpContext ctx) {
		Log.info("Entering OperatorComparison Expression");
		
		visitExp(ctx.exp(0));
		visitExp(ctx.exp(1));
		
		instructions.add("COMPARE " + ctx.operatorComparison().getText());
		
		return 0;
	}
	
	@Override
	public Integer visitOperatorAndExp(OperatorAndExpContext ctx) {
		Log.info("Entering OperatorAnd Expression");
		
		visitExp(ctx.exp(0));
		visitExp(ctx.exp(1));
		
		instructions.add("COMPARE AND");
		
		return 0;
	}

	@Override
	public Integer visitOperatorOrExp(OperatorOrExpContext ctx) {
		Log.info("Entering OperatorOr Expression");
		
		visitExp(ctx.exp(0));
		visitExp(ctx.exp(1));
		
		instructions.add("COMPARE OR");
		
		return 0;
	}

	@Override
	public Integer visitOperatorUnary(OperatorUnaryContext ctx) {
		return 0;
	}

	@Override
	public Integer visitOperatorAnd(OperatorAndContext ctx) {
		return 0;
	}

	@Override
	public Integer visitBooleandt(BooleandtContext ctx) {
		return 0;
	}

	@Override
	public Integer visitNumberdt(NumberdtContext ctx) {
		return 0;
	}

	@Override
	public Integer visitOperatorComparison(OperatorComparisonContext ctx) {
		return 0;
	}

	@Override
	public Integer visitStringdt(StringdtContext ctx) {
		return 0;
	}

	@Override
	public Integer visitOperatorPower(OperatorPowerContext ctx) {
		return 0;
	}

	@Override
	public Integer visitOperatorOr(OperatorOrContext ctx) {
		return 0;
	}

	@Override
	public Integer visitOperatorAddSub(OperatorAddSubContext ctx) {
		return 0;
	}

	@Override
	public Integer visitOperatorMulDivMod(OperatorMulDivModContext ctx) {
		return 0;
	}

	@Override
	public Integer visitListdt(ListdtContext ctx) {
		return 0;
	}

	public Integer visitPositiondt(PositiondtContext ctx) {
		// TODO : There are 4 other contexts here, which must be implemented if
		// this function is ever needed.
		
		return 0;
	}

	@Override
	public Integer visitObjectdt(ObjectdtContext ctx) {
		Log.info("Entering Objectdt");
		
		return 0;
	}
	
	@Override
	public Integer visitOperatorStrcat(OperatorStrcatContext ctx) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	// OTHERS ===================================
	
	@Override
	public Integer visitChildren(RuleNode arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer visitErrorNode(ErrorNode arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer visitTerminal(TerminalNode arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String currentLabel() {
		return "l" + conditionalLabelCounter;
	}
	
	public String nextLabel() {
		conditionalLabelCounter++;
		return "l" + conditionalLabelCounter;
	}

	@Override
	public Integer visitPositiondtRangeNumber(PositiondtRangeNumberContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitPositiondtRangeIdentifier(PositiondtRangeIdentifierContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitPositiondtNumber(PositiondtNumberContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitPositiondtIdentifier(PositiondtIdentifierContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitPositiondtArrayIdentifierNumber(PositiondtArrayIdentifierNumberContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visitPositiondtArrayIdentifierIdentifier(PositiondtArrayIdentifierIdentifierContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}
}