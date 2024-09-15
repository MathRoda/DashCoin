//
//  DashCoinIOSApp.swift
//  DashCoinIOS
//
//  Created by Mouawia Hammo on 8/9/2024.
//

import SwiftUI
import shared

@main
struct DashCoinIOSApp: App {
    
    init() {
        KoinKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView().edgesIgnoringSafeArea(.all)
        }
    }
}
