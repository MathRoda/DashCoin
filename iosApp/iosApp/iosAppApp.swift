//
//  iosAppApp.swift
//  iosApp
//
//  Created by Mouawia Hammo on 16/9/2024.
//

import SwiftUI
import shared

@main
struct iosAppApp: App {
    
    init() {
        MainViewControllerKt.doInitFirebase()
        KoinKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView().edgesIgnoringSafeArea(.all)
        }
    }
}
