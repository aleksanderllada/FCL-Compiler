package com.fcl.compiler.antlr4;

// Generated from FCL.g4 by ANTLR 4.5.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link FCLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface FCLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link FCLParser#chunk}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChunk(FCLParser.ChunkContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(FCLParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by the {@code semicolonStat}
	 * labeled alternative in {@link FCLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSemicolonStat(FCLParser.SemicolonStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code breakStat}
	 * labeled alternative in {@link FCLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreakStat(FCLParser.BreakStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whileStat}
	 * labeled alternative in {@link FCLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStat(FCLParser.WhileStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code forStat}
	 * labeled alternative in {@link FCLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStat(FCLParser.ForStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code foreachStat}
	 * labeled alternative in {@link FCLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForeachStat(FCLParser.ForeachStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifStat}
	 * labeled alternative in {@link FCLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStat(FCLParser.IfStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code procedurecallStat}
	 * labeled alternative in {@link FCLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedurecallStat(FCLParser.ProcedurecallStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code procedureStat}
	 * labeled alternative in {@link FCLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedureStat(FCLParser.ProcedureStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code attrStat}
	 * labeled alternative in {@link FCLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttrStat(FCLParser.AttrStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code printexpStat}
	 * labeled alternative in {@link FCLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintexpStat(FCLParser.PrintexpStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code moveStat}
	 * labeled alternative in {@link FCLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMoveStat(FCLParser.MoveStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pickupStat}
	 * labeled alternative in {@link FCLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPickupStat(FCLParser.PickupStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code discardStat}
	 * labeled alternative in {@link FCLParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDiscardStat(FCLParser.DiscardStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#procedure}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure(FCLParser.ProcedureContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#procedurecall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedurecall(FCLParser.ProcedurecallContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#objlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjlist(FCLParser.ObjlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#attr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttr(FCLParser.AttrContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#varlist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarlist(FCLParser.VarlistContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#retstat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetstat(FCLParser.RetstatContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#explist}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplist(FCLParser.ExplistContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#printexp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintexp(FCLParser.PrintexpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code listdtExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListdtExp(FCLParser.ListdtExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code identifierExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierExp(FCLParser.IdentifierExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operatorOrExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorOrExp(FCLParser.OperatorOrExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nilExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNilExp(FCLParser.NilExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringdtExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringdtExp(FCLParser.StringdtExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operatorPowerExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorPowerExp(FCLParser.OperatorPowerExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code numberdtExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberdtExp(FCLParser.NumberdtExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code booleandtExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleandtExp(FCLParser.BooleandtExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operatorMulDivModExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorMulDivModExp(FCLParser.OperatorMulDivModExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code objectdtExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectdtExp(FCLParser.ObjectdtExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operatorAddSubExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorAddSubExp(FCLParser.OperatorAddSubExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code procedurecallExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedurecallExp(FCLParser.ProcedurecallExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code positiondtExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositiondtExp(FCLParser.PositiondtExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operatorAndExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorAndExp(FCLParser.OperatorAndExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operatorUnaryExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorUnaryExp(FCLParser.OperatorUnaryExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operatorComparisonExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorComparisonExp(FCLParser.OperatorComparisonExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operatorStrcatExp}
	 * labeled alternative in {@link FCLParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorStrcatExp(FCLParser.OperatorStrcatExpContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#operatorOr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorOr(FCLParser.OperatorOrContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#operatorAnd}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorAnd(FCLParser.OperatorAndContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#operatorComparison}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorComparison(FCLParser.OperatorComparisonContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#operatorStrcat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorStrcat(FCLParser.OperatorStrcatContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#operatorAddSub}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorAddSub(FCLParser.OperatorAddSubContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#operatorMulDivMod}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorMulDivMod(FCLParser.OperatorMulDivModContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#operatorUnary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorUnary(FCLParser.OperatorUnaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#operatorPower}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorPower(FCLParser.OperatorPowerContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#numberdt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberdt(FCLParser.NumberdtContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#booleandt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleandt(FCLParser.BooleandtContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#stringdt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringdt(FCLParser.StringdtContext ctx);
	/**
	 * Visit a parse tree produced by the {@code positiondtRangeIdentifier}
	 * labeled alternative in {@link FCLParser#positiondt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositiondtRangeIdentifier(FCLParser.PositiondtRangeIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code positiondtRangeNumber}
	 * labeled alternative in {@link FCLParser#positiondt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositiondtRangeNumber(FCLParser.PositiondtRangeNumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code positiondtIdentifier}
	 * labeled alternative in {@link FCLParser#positiondt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositiondtIdentifier(FCLParser.PositiondtIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code positiondtNumber}
	 * labeled alternative in {@link FCLParser#positiondt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositiondtNumber(FCLParser.PositiondtNumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code positiondtArrayIdentifierIdentifier}
	 * labeled alternative in {@link FCLParser#positiondt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositiondtArrayIdentifierIdentifier(FCLParser.PositiondtArrayIdentifierIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code positiondtArrayIdentifierNumber}
	 * labeled alternative in {@link FCLParser#positiondt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPositiondtArrayIdentifierNumber(FCLParser.PositiondtArrayIdentifierNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#objectdt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectdt(FCLParser.ObjectdtContext ctx);
	/**
	 * Visit a parse tree produced by {@link FCLParser#listdt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListdt(FCLParser.ListdtContext ctx);
}