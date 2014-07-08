package org.teamweaver.delias.utils;

import java.util.Date;

public class DEvent{

	public String kind;
	public Date dat;
	public Date endDate;
	public String originID;
	public String structureKind;
	public String structureHandle;
	public String navigation;
	public String delta;
	public String interestContribution;
	public String concerns;

	@Override
	public String toString(){
		return kind+" "+concerns+" "+dat+" "+endDate+" "+originID+" "
	+structureKind+" "+structureHandle+" "+navigation+" "+delta;
	}
}

