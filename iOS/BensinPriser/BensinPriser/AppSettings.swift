//
//  AppSettings.swift
//  BensinPriser
//
//  Created by Andrian Sergheev on 2020-01-31.
//  Copyright © 2020 Andrian Sergheev. All rights reserved.
//

import SwiftUI
import Foundation

struct BColors {
	static let red: UIColor = UIColor(red: 240/255, green: 19/255, blue: 77/255, alpha: 255)
}

struct BFonts {
	
}

final class AppSettings {

	static let shared = AppSettings()

	private init () { settings() }

	private func settings () {

	}

}
