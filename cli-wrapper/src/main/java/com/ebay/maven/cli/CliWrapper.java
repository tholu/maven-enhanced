/*
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation. 
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */
package com.ebay.maven.cli;

import java.util.List;

import org.apache.commons.cli.ParseException;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Repository;

import com.ebay.maven.utils.PomUtils;

/**
 * <code>CliWrapper</code> prepares the workspace before maven kicks in.
 * <ol>
 * 	<li>Downloads all dependencencies and pre-fill the local repository before maven starts.</li>
 * 	<li>If the project has a <I>Compiled Source Repository</I>, it gets the class files into target/classes folder.
 * </ol>
 *
 * <H3>Downloading All Dependencies</H3>
 * reads pom file(s) and collects the repositories and dependencies the project needs.
 * The wrapper should be able to process all the CLI parameters that maven takes and pass it on to maven.
 * 
 * <H3>Using compiled repository</H3>
 * large projects may have 1000s of java classes, it may take a long time to build in a laptop.
 * So, lightening will download the pre-compiled classes for the project and fill 'target' folder.
 * This will enable the developer to get start coding in seconds. 
 * 
 * 
 * @author nambi sankaran
 *
 */
public class CliWrapper {

	public static void main( String[] args ){
		
		CliWrapper wrapper = new CliWrapper();
		InputParams input = wrapper.processCliArguments(args);
		
		wrapper.process(input);
	}
	
	public InputParams processCliArguments( String[] args ){
		
		InputParams input = null;
		
		CliArgsParser parser = new CliArgsParser();
		try {
			
			input = parser.parse(args);
			
			if( input.getMode().equals(RunMode.USAGE ) ){
				parser.printUsage();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return input;
	}
	
	public void process( InputParams input ){
		
		if( input.getMode().equals(RunMode.CREATE_UPDATE) ){
					createOrUpdateBinaryRepository();
		}
	}
	
	public void downloadDependencies(){
		// read the pom.xml
		// TODO: get the pom.xml path from -f argument
		Model model = PomUtils.readModel("pom.xml");
		
		// collect the repositories in the correct order
		List<Repository> repositories= model.getRepositories();
		
		// collect the dependencies
		List<Dependency> dependencies = model.getDependencies();
		
		// TODO: read the settings.xml to collect the repositories
		
		// construct the JSON request 
		
		// call nexus repository with JSON post
		
		// download the dependencies
		
		// invoke maven with dependencies
	}
	
	public void createOrUpdateBinaryRepository(){
		
		// assume the current directory the "root" of the project
	}

}
