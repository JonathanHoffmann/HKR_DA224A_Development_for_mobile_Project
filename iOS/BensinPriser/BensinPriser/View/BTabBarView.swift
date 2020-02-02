//
//  BTabBarView.swift
//  BensinPriser
//
//  Created by Andrian Sergheev on 2020-01-31.
//  Copyright © 2020 Andrian Sergheev. All rights reserved.
//

import SwiftUI

struct BTabBarView: View {

	var body: some View {
		TabView {
			MainView()
				.tabItem {
					Image(systemName: "flame.fill")
					Text("Prices")
			}
			SettingsView()
				.tabItem {
					Image(systemName: "hammer")
					Text("Settings")
			}
		}
	}
}

struct BTabBarView_Previews: PreviewProvider {
	static var previews: some View {
		BTabBarView()
	}
}
