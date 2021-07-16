package com.abnamro.nl.channels.geninfo.bankmail.interfaces;

import java.util.List;

import com.abnamro.nl.dto.util.AbstractDTO;

/**
 * FolderListDTO 
 * 
 * <PRE>
 * <B>History:</B>
 * Developer          Date       Change Reason	  Change
 * ------------------ ---------- ----------------- ----------------------------------------------
 * TCS			  	29-05-2012	Initial version	  Release 1.0
 * </PRE>
 * @author 
 * @see
 */
public class FolderListDTO extends AbstractDTO {
	private static final long serialVersionUID = 1L;
	/**
	 * List of folders.
	 */
	private List<FolderCustomDTO> folders;

	/**
	 * @return folders List<FolderCustomDTO>
	 */
	public List<FolderCustomDTO> getFolders() {
		return folders;
	}

	/**
	 * @param folders List<FolderCustomDTO> 
	 */
	public void setFolders(List<FolderCustomDTO> folders) {
		this.folders = folders;
	}	
	
}
