package com.statestr.arvin.util;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class GitUtil {

	private static String localCodeDir = "C:\\Users\\a805370\\Test\\ZeusUI";
	private static String remoteRepoGitDir = "http://jabdl3824.it.statestr.com/Athena/ZeusUI.git";

	public static String newBranch(String branchName) {
		return null;

	}

	public static boolean cloneCodeToLocal(String localCodeDir, String remoteRepoGitDir, String username, String password) {
		boolean resultFlag = false;
		File file = new File(localCodeDir);
		if (file.exists()) {
			deleteFolder(file);
		}
		try {
//			git = Git.open(new File(localCodeDir));
//			git.cloneRepository().setURI(localRepoGitConfig).setDirectory(file).call();
			
			CloneCommand cloneCommand = Git.cloneRepository().setURI(remoteRepoGitDir);
			cloneCommand.setDirectory(new File(localCodeDir));
			cloneCommand.setCredentialsProvider( new UsernamePasswordCredentialsProvider( username, password ) ).call();
			
			resultFlag = true;
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		} 
		return resultFlag;
	}
	
	public static boolean pullCodeToLocal(String localCodeDir, String username, String password) {
		boolean resultFlag = false;
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(username, password);
		try {
			Git.open(new File(localCodeDir)).pull().setCredentialsProvider(cp).call();
			resultFlag = true;
		} catch (GitAPIException | IOException e) {
			e.printStackTrace();
		}
		return resultFlag;
	}

	private static void deleteFolder(File file) {
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File f : files) {
				deleteFolder(f);
				f.delete();
			}
		} else {
			file.delete();
		}
	}
	
	public static void main(String[] args) {
//		System.out.print(pullCodeToLocal(localCodeDir, "a805370", "Hugc!1225"));
		
	}

}
