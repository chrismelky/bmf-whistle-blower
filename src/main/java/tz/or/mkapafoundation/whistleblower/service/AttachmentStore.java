package tz.or.mkapafoundation.whistleblower.service;

import org.springframework.content.commons.repository.ContentStore;
import org.springframework.stereotype.Component;

import tz.or.mkapafoundation.whistleblower.domain.Attachment;

@Component
public interface AttachmentStore extends ContentStore<Attachment,String> {
    
}