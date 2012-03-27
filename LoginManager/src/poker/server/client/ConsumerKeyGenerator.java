/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package poker.server.client;

import poker.server.client.widgets.LoginWidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ConsumerKeyGenerator implements EntryPoint {
	

	public void onModuleLoad() {
		RootPanel rootPanel = RootPanel.get();
		
		Image img = new Image("/PokerLogo.jpg");
		img.setSize("200px", "100px");
		rootPanel.add(img);

		LoginWidget login = new LoginWidget();
		login.setStyleName("LoginWidget");
		rootPanel.add(login);
	}
}
