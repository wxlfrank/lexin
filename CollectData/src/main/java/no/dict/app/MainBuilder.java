package no.dict.app;

import no.dict.threads.BuildDictionary;

public class MainBuilder {

	public static void main(String[] args) {
		BuildDictionary builder = new BuildDictionary();
		builder.run();
	}

}
