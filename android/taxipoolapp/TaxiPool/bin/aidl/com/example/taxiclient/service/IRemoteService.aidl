package com.example.taxiclient.service;

import com.example.taxiclient.service.IRemoteServiceCallback;

interface IRemoteService {
	boolean registerCallback(IRemoteServiceCallback callback);
	boolean unregisterCallback(IRemoteServiceCallback callback);
}