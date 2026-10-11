package com.prepwise.service;

import com.prepwise.dto.request.gd.SendGDMessageRequest;
import com.prepwise.dto.request.gd.StartGDRequest;
import com.prepwise.dto.response.gd.GDHistoryItemResponse;
import com.prepwise.dto.response.gd.GDReportResponse;
import com.prepwise.dto.response.gd.GDSessionResponse;
import com.prepwise.dto.response.gd.SendGDMessageResponse;

import java.util.List;

public interface GroupDiscussionService {

    GDSessionResponse startDiscussion(String email, StartGDRequest request);

    SendGDMessageResponse sendMessage(String email, Long sessionId, SendGDMessageRequest request);

    GDSessionResponse getSession(String email, Long sessionId);

    GDReportResponse finishDiscussion(String email, Long sessionId);

    GDReportResponse getReport(String email, Long sessionId);

    List<GDHistoryItemResponse> getHistory(String email);

    GDSessionResponse retryDiscussion(String email, Long sessionId);

    void deleteSession(String email, Long sessionId);
}
