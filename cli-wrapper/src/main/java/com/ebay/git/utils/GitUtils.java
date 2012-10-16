package com.ebay.git.utils;

import com.ebay.github.client.GitHubClient;
import com.google.common.base.Strings;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.StoredConfig;
import org.kohsuke.github.GHUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class GitUtils {
	
	public static String getRepositoryName( String repository ){
		
		String repositoryName=null;
		
		if( repository == null || repository.trim().equals("")){
			throw new NullPointerException("Repository name cannot be null or empty");
		}
		
		// git ssh format
		// git@github.com:snambi/myrepo.git
		if( repository.contains( "@" )){
			String[]  s = repository.split("/");
			
			if( s != null && s.length >=2 ){
				
				// take the last one
				String gitrepo = s[s.length-1];
				
				if( gitrepo.contains(".git")){
					repositoryName = getRepo(gitrepo);
				}
			}
		}
		
		// git readonly format
		// git://github.com/snambi/maven-enhanced.git
		if( repository.startsWith("git://")){
			
			String[] s = repository.split("/");
			
			if( s!= null  && s.length >= 2 ){
				repositoryName = getRepo(s[ s.length -1 ]);
			}
		}
		
		// http format
		// https://github.com/snambi/myrepo.git
		if( repository.startsWith("http")){
			
			String[] s = repository.split("\\/");
			if( s!= null  && s.length >= 2 ){
				repositoryName = getRepo(s[ s.length -1 ]);
			}
		}
		
		return repositoryName;
	}
	
	public static String getOrgName( String repository ){
		
		String orgName=null;
		
		if( repository == null || repository.trim().equals("")){
			throw new NullPointerException("Repository name cannot be null or empty");
		}
		
		// git ssh format
		// git@github.com:snambi/myrepo.git
		if( repository.contains( "@" )){
			String[]  s = repository.split(":");
			
			if( s != null && s.length == 2 ){
				
				// take the first second one
				String gitrepo = s[1];
				
				if( gitrepo.contains(".git")){
					if( gitrepo.contains("/")){
						String o[] = gitrepo.split("/");
						if( o.length == 2){
							orgName = o[0];
						}
					}
				}
			}
		}
		
		// git readonly format
		// git://github.com/snambi/maven-enhanced.git
		if( repository.startsWith("git://")){
			
			String[] s = repository.split("/");
			
			if( s!= null  && s.length >= 3 ){
				orgName = s[ s.length -2 ];
			}
		}
		
		// http format
		// https://github.com/snambi/myrepo.git
		if( repository.startsWith("http")){
			
			String[] s = repository.split("\\/");
			if( s!= null  && s.length >= 3 ){
				orgName = s[ s.length -2 ];
			}
		}
		
		return orgName;
	}
	
	/**
	 * extracts repository name from string of the format "repo.git".
	 * @param repository
	 * @return
	 */
	public static String getRepo(String input ){
		String repository=null;
		
		if( input != null && !input.trim().equals("")){
			
			if( input.contains(".git")){
				String[] a = input.split("\\.");
				repository = a[0];
			}else{
				repository = input;
			}
			
		}
		
		return repository;
	}

	public static Collection<String> getFilesToStage( StatusCommand statusCmd ){
		
		Collection<String> tobeAdded = new ArrayList<String>();
		
		try {
			Status status = statusCmd.call();
			
			
			Collection<String> issues = new ArrayList<String>();
			
			tobeAdded.addAll(status.getModified());
			tobeAdded.addAll(status.getChanged());
			tobeAdded.addAll(status.getRemoved());
			tobeAdded.addAll(status.getUntracked());
			
			for( String file : tobeAdded){
				System.out.println(file);
			}
			
			issues.addAll(status.getConflicting());
			issues.addAll(status.getMissing());
			
			for( String file: issues ){
				// TODO: what to do? throw error
				System.out.println("BAD: " + file);
			}
			
		} catch (NoWorkTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tobeAdded;
	}

    public static boolean existsInGit(final String repo) throws IOException {
        if (Strings.isNullOrEmpty(repo)) return false;

        final org.eclipse.jgit.lib.Repository repository = new org.eclipse.jgit.storage.file.FileRepository(repo);
        final StoredConfig config = repository.getConfig();
        final String name = config.getString("user", null, "name");
        final String email = config.getString("user", null, "email");
        String[] split = new String[2];
        if (name == null || email == null) {
            System.out.println("User identity is unknown!");
        } else {
            split = email.split("@");
            System.out.println("User identity is " + name + " <" + email + ">");
        }

        String userName = null;
        if (split.length > 0 ) {
            userName = split[0];
        }

        if (!Strings.isNullOrEmpty(userName)) {
            final GHUser user = new GitHubClient().connect().getUser(userName);
            if (user != null) {
                return user.getRepository(repo) != null;
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("MAIN" + GitUtils.existsInGit("binrepo-devex"));
        System.out.println("MAIN" + GitUtils.existsInGit("CreatedUsingGitHub-API-Client"));
    }


}
