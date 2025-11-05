package reactors;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import prerna.sablecc2.om.GenRowStruct;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.ReactorKeysEnum;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.reactor.AbstractReactor;

/**
 * This reactor shows examples of how to define and get various input types
 * passed into the reactor.
 * 
 * This reactor can be tested in the pixel console in SEMOSS by running:
 * Input(name=["John"], comment=["Hello"], map=[{"test":"valueKey"}], age=[18], hasAccess=[true], colors=["red", "blue"]);
 */
public class InputReactor extends AbstractReactor {
	public static final String NAME_INPUT_KEY = "name";
	public static final String AGE_INPUT_KEY = "age";
	public static final String MAP_INPUT_KEY = "map";

	/*
	 * Reactors define the inputs in the constructor
	 */
	public InputReactor() {
		// keys can be custom or generic from the @ReactorKeysEnum
		this.keysToGet = new String[] { NAME_INPUT_KEY, ReactorKeysEnum.COMMENT_KEY.getKey(), MAP_INPUT_KEY,
				"hasAccess", AGE_INPUT_KEY, "colors" };
	}

	public NounMetadata execute() {
		// to grab the string inputs
		organizeKeys();
		// index based on input key order defined in keysToGet
		String name = this.keyValue.get(this.keysToGet[0]);
		String comment = this.keyValue.get(this.keysToGet[1]);

		// grab numeric input
		int age = getAge();
		// grab map input
		Map<String, Object> mapInput = getMapInput();
		// printing map input
		for (String key : mapInput.keySet()) {
			String value = mapInput.get(key).toString();
			System.out.println("key: " + key + " value: " + value);
		}
		boolean hasAccess = getBoolean();
		List<String> columns = getListInput();

		// create output
		StringBuilder output = new StringBuilder();
		output.append("name: " + name);
		output.append(" age: " + age);
		output.append(" comment: " + comment);
		output.append(" hasAccess: " + hasAccess);
		output.append(" favorite color(s): " + Arrays.toString(columns.toArray()));
		return new NounMetadata(output.toString(), PixelDataType.CONST_STRING);
	}

	private List<String> getListInput() {
		List<String> colInputs = new Vector<String>();
		GenRowStruct colGRS = this.store.getNoun("colors");
		if (colGRS != null) {
			for (int i = 0; i < colGRS.size(); i++) {
				String stringValue = colGRS.get(i).toString();
				colInputs.add(stringValue);
			}
		}
		return colInputs;
	}

	private int getAge() {
		int input = 0;
		GenRowStruct boolGrs = this.store.getNoun(AGE_INPUT_KEY);
		if (boolGrs != null && !boolGrs.isEmpty()) {
			List<Object> val = boolGrs.getValuesOfType(PixelDataType.CONST_INT);
			input = (int) val.get(0);
		}
		return input;
	}

	private boolean getBoolean() {
		boolean input = false;
		GenRowStruct boolGrs = this.store.getNoun("hasAccess");
		if (boolGrs != null && !boolGrs.isEmpty()) {
			List<Object> val = boolGrs.getValuesOfType(PixelDataType.BOOLEAN);
			input = (boolean) val.get(0);
		}
		return input;
	}

	private Map<String, Object> getMapInput() {
		GenRowStruct mapGrs = this.store.getNoun(MAP_INPUT_KEY);
		if (mapGrs != null && !mapGrs.isEmpty()) {
			List<NounMetadata> mapInputs = mapGrs.getNounsOfType(PixelDataType.MAP);
			if (mapInputs != null && !mapInputs.isEmpty()) {
				return (Map<String, Object>) mapInputs.get(0).getValue();
			}
		}
		return null;
	}
}
