package reactors;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import prerna.sablecc2.om.GenRowStruct;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.reactor.AbstractReactor;

/**
 * This reactor shows examples of how to output various types from the reactor.
 * Reactors define the output in the NounMetadata object that is returned in execute.
 * 
 * This reactor can be tested in the pixel console in SEMOSS by running:
 * Output(value=[5.6]); 
 * Output(value=[[1,2,3]]); 
 * Output();
 */
public class OutputReactor extends AbstractReactor {

	public OutputReactor() {
		this.keysToGet = new String[] { "value" };
	}

	public NounMetadata execute() {
		GenRowStruct mapGrs = this.store.getNoun("value");
		// return null value
		if (mapGrs == null) {
			return new NounMetadata(null, PixelDataType.NULL_VALUE);
		}
		// return a vector
		if (mapGrs.size() > 0) {
			// process list input
			List<Object> list = new Vector<Object>();
			for (int i = 0; i < mapGrs.size(); i++) {
				list.add(mapGrs.get(i));
			}
			return new NounMetadata(list, PixelDataType.VECTOR);
		}
		Object value = mapGrs.get(0);
		System.out.println(value.getClass());
		if (value instanceof Integer) { // return an int
			return new NounMetadata(value, PixelDataType.CONST_INT);
		} else if (value instanceof Double) { // return a double
			return new NounMetadata(value, PixelDataType.CONST_DECIMAL);
		} else if (value instanceof Map) { // return a map
			return new NounMetadata(value, PixelDataType.MAP);
		} else {// default to return string
			return new NounMetadata(value.toString(), PixelDataType.CONST_STRING);
		}
	}
}
